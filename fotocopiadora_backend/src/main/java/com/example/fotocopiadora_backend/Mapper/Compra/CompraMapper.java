package com.example.fotocopiadora_backend.Mapper.Compra;

import com.example.fotocopiadora_backend.Dto.Compra.Request.CompraRequestDto;
import com.example.fotocopiadora_backend.Dto.Compra.Request.DetalleCompraRequestDto;
import com.example.fotocopiadora_backend.Dto.Compra.Response.CompraResponseDto;
import com.example.fotocopiadora_backend.Dto.Compra.Response.DetalleCompraResponseDto;
import com.example.fotocopiadora_backend.Entity.Compra.Compra;
import com.example.fotocopiadora_backend.Entity.Compra.DetalleCompra;
import com.example.fotocopiadora_backend.Entity.Producto.Producto;
import com.example.fotocopiadora_backend.Enum.EstadoCompra;
import com.example.fotocopiadora_backend.Enum.TipoProducto;
import com.example.fotocopiadora_backend.Repository.Producto.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CompraMapper {
    private final ProductoRepository productoRepository;

    public Compra toEntity(CompraRequestDto dto) {
        Compra compra = new Compra();
        compra.setProveedor(dto.getProveedor());
        compra.setMonto(dto.getMonto());
        compra.setEstadoCompra(EstadoCompra.PENDIENTE);

        List<DetalleCompra> detalles = new ArrayList<>();
        Set<Long> productosId = new HashSet<>();

        for(DetalleCompraRequestDto detalleDto : dto.getDetalleCompra()) {
            Long idProducto = detalleDto.getIdProducto();

            if(!productosId.add(idProducto)){
                throw new IllegalArgumentException("El producto ya esta registrado en la compra");
            }

            Producto producto = productoRepository.findByIdAndSoftDeleteFalse(idProducto)
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + detalleDto.getIdProducto()));

            if(!(producto.getTipoProducto() == TipoProducto.PRODUCTO_VENTA || producto.getTipoProducto() == TipoProducto.INSUMO)){
                throw new IllegalArgumentException("El producto no es válido para compra");
            }

            DetalleCompra detalle = new DetalleCompra();
            detalle.setCompra(compra);
            detalle.setProducto(producto);
            detalle.setCantidad(detalleDto.getCantidad());
            detalle.setEstadoCompra(EstadoCompra.PENDIENTE);

            detalles.add(detalle);
        }

        compra.setDetalleCompra(detalles);
        return compra;
    }

    public CompraResponseDto toDto(Compra compra) {
        CompraResponseDto dto = new CompraResponseDto();
        dto.setId(compra.getId());
        dto.setProveedor(compra.getProveedor());
        dto.setMonto(compra.getMonto());
        dto.setFechaPago(compra.getFechaPago());
        dto.setEstadoCompra(compra.getEstadoCompra());
        dto.setSoftDelete(compra.isSoftDelete());

        List<DetalleCompraResponseDto> detallesDto = new ArrayList<>();
        for (DetalleCompra detalle : compra.getDetalleCompra()) {
            DetalleCompraResponseDto detalleDto = new DetalleCompraResponseDto();
            detalleDto.setProducto(detalle.getProducto().getNombre());
            detalleDto.setCantidad(detalle.getCantidad());
            detalleDto.setEstadoCompra(detalle.getEstadoCompra());
            detallesDto.add(detalleDto);
        }
        dto.setDetalleCompra(detallesDto);
        return dto;
    }

    public List<CompraResponseDto> toDtoList(List<Compra> compras) {
        return compras.stream()
                .map(this::toDto)
                .toList();
    }

    public void updateEntityFromDto(Compra compra, CompraRequestDto dto) {
        compra.setProveedor(dto.getProveedor());
        compra.setMonto(dto.getMonto());

        List<DetalleCompra> detalles = new ArrayList<>();
        Set<Long> productosId = new HashSet<>();

        for (DetalleCompraRequestDto detalleDto : dto.getDetalleCompra()) {
            Long idProducto = detalleDto.getIdProducto();

            if (!productosId.add(idProducto)) {
                throw new IllegalArgumentException("El producto ya esta registrado en la compra");
            }

            Producto producto = productoRepository.findByIdAndSoftDeleteFalse(idProducto)
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + detalleDto.getIdProducto()));

            if (!(producto.getTipoProducto() == TipoProducto.PRODUCTO_VENTA || producto.getTipoProducto() == TipoProducto.INSUMO)) {
                throw new IllegalArgumentException("El producto no es válido para compra");
            }

            DetalleCompra detalle = new DetalleCompra();
            detalle.setCompra(compra);
            detalle.setProducto(producto);
            detalle.setCantidad(detalleDto.getCantidad());
            detalle.setEstadoCompra(EstadoCompra.PENDIENTE);

            detalles.add(detalle);
        }
        compra.getDetalleCompra().clear();
        compra.getDetalleCompra().addAll(detalles);
    }
}
