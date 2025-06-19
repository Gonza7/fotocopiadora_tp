package com.example.fotocopiadora_backend.Mapper.Venta;

import com.example.fotocopiadora_backend.Dto.Venta.Request.DetalleVentaRequestDto;
import com.example.fotocopiadora_backend.Dto.Venta.Request.VentaRequestDto;
import com.example.fotocopiadora_backend.Dto.Venta.Response.DetalleVentaResponseDto;
import com.example.fotocopiadora_backend.Dto.Venta.Response.VentaResponseDto;
import com.example.fotocopiadora_backend.Entity.Producto.PrecioFotocopia;
import com.example.fotocopiadora_backend.Entity.Producto.Producto;
import com.example.fotocopiadora_backend.Entity.Venta.DetalleVenta;
import com.example.fotocopiadora_backend.Entity.Venta.Venta;
import com.example.fotocopiadora_backend.Enum.TipoProducto;
import com.example.fotocopiadora_backend.Repository.Producto.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class VentaMapper {
    private final ProductoRepository productoRepository;

    public Venta toEntity(VentaRequestDto dto) {
        Venta venta = new Venta();
        venta.setFormaPago(dto.getFormaPago());

        List<DetalleVenta> detalles = new ArrayList<>();
        double total = 0;

        Set<Long> productosId = new HashSet<>();

        for (DetalleVentaRequestDto detalleDto : dto.getDetallesVenta()) {
            Long idProducto = detalleDto.getIdProducto();

            if(!productosId.add(idProducto)){
                throw new IllegalArgumentException("El producto ya esta registrado en la venta");
            }

            Producto producto = productoRepository.findByIdAndSoftDeleteFalse(idProducto)
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + detalleDto.getIdProducto()));

            if(!(producto.getTipoProducto() == TipoProducto.PRODUCTO_VENTA || producto.getTipoProducto() == TipoProducto.FOTOCOPIA)){
                throw new IllegalArgumentException("El producto no es válido para venta");
            }

            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);
            detalle.setProducto(producto);
            detalle.setCantidad(detalleDto.getCantidad());

            double precioUnitario = obtenerPrecioUnitario(producto, detalleDto.getCantidad());
            detalle.setPrecioUnitario(precioUnitario);

            total += precioUnitario * detalleDto.getCantidad();
            detalles.add(detalle);
        }

        venta.setDetalleVenta(detalles);
        venta.setPrecioTotal(total);

        return venta;
    }
    private double obtenerPrecioUnitario(Producto producto, int cantidad) {
        switch (producto.getTipoProducto()) {
            case PRODUCTO_VENTA:
            case INSUMO:
                return producto.getPrecioUnitario();
            case FOTOCOPIA:
                return producto.getPreciosFotocopia().stream()
                        .filter(pf -> cantidad >= pf.getMinimo())
                        .max(Comparator.comparingInt(PrecioFotocopia::getMinimo))
                        .orElseThrow(() -> new IllegalArgumentException("No hay precio válido para la cantidad ingresada"))
                        .getPrecioUnitario();
            default:
                throw new IllegalArgumentException("Tipo de producto no reconocido");
        }
    }

    public VentaResponseDto toDto(Venta venta) {
        VentaResponseDto dto = new VentaResponseDto();
        dto.setId(venta.getId());
        dto.setFormaPago(venta.getFormaPago());
        dto.setFechaPago(venta.getFechaPago());
        dto.setPrecioTotal(venta.getPrecioTotal());
        dto.setSoftDelete(venta.isSoftDelete());
        List<DetalleVentaResponseDto> detallesDto = new ArrayList<>();
        for (DetalleVenta detalle : venta.getDetalleVenta()) {
            DetalleVentaResponseDto detalleDto = new DetalleVentaResponseDto();
            detalleDto.setProducto(detalle.getProducto().getNombre());
            detalleDto.setCantidad(detalle.getCantidad());
            detalleDto.setPrecioUnitario(detalle.getPrecioUnitario());
            detallesDto.add(detalleDto);
        }
        dto.setDetallesVenta(detallesDto);

        return dto;
    }
    public List<VentaResponseDto> toDtoList(List<Venta> ventas) {
        return ventas.stream()
                .map(this::toDto)
                .toList();
    }

    public void updateEntityFromDto(Venta venta, VentaRequestDto dto) {
        venta.setFormaPago(dto.getFormaPago());

        List<DetalleVenta> detalles = new ArrayList<>();
        double total = 0;

        Set<Long> productosId = new HashSet<>();

        for (DetalleVentaRequestDto detalleDto : dto.getDetallesVenta()) {
            Long idProducto = detalleDto.getIdProducto();

            if(!productosId.add(idProducto)){
                throw new IllegalArgumentException("El producto ya esta registrado en la venta");
            }

            Producto producto = productoRepository.findByIdAndSoftDeleteFalse(idProducto)
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + detalleDto.getIdProducto()));

            if(!(producto.getTipoProducto() == TipoProducto.PRODUCTO_VENTA || producto.getTipoProducto() == TipoProducto.FOTOCOPIA)){
                throw new IllegalArgumentException("El producto no es válido para venta");
            }

            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);
            detalle.setProducto(producto);
            detalle.setCantidad(detalleDto.getCantidad());

            DetalleVenta existingDetalle = venta.getDetalleVenta().stream()
                    .filter(d -> d.getProducto().getId().equals(producto.getId()))
                    .findFirst()
                    .orElse(null);

            double precioUnitario;

            if(existingDetalle != null && existingDetalle.getProducto().getId().equals(producto.getId())){
                precioUnitario = existingDetalle.getPrecioUnitario();
            } else {
                precioUnitario = obtenerPrecioUnitario(producto, detalleDto.getCantidad());
            }
            detalle.setPrecioUnitario(precioUnitario);

            total += precioUnitario * detalleDto.getCantidad();
            detalles.add(detalle);
        }

        venta.getDetalleVenta().clear();
        venta.getDetalleVenta().addAll(detalles);
        venta.setPrecioTotal(total);
    }
}
