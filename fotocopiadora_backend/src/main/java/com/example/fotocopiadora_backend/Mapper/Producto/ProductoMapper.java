package com.example.fotocopiadora_backend.Mapper.Producto;

import com.example.fotocopiadora_backend.Dto.Producto.PrecioFotocopiaDto;
import com.example.fotocopiadora_backend.Dto.Producto.Request.FotocopiaRequestDto;
import com.example.fotocopiadora_backend.Dto.Producto.Request.InsumoRequestDto;
import com.example.fotocopiadora_backend.Dto.Producto.Request.ProductoRequestDto;
import com.example.fotocopiadora_backend.Dto.Producto.Request.ProductoVentaRequestDto;
import com.example.fotocopiadora_backend.Dto.Producto.Response.FotocopiaResponseDto;
import com.example.fotocopiadora_backend.Dto.Producto.Response.InsumoResponseDto;
import com.example.fotocopiadora_backend.Dto.Producto.Response.ProductoResponseDto;
import com.example.fotocopiadora_backend.Dto.Producto.Response.ProductoVentaResponseDto;
import com.example.fotocopiadora_backend.Entity.Producto.PrecioFotocopia;
import com.example.fotocopiadora_backend.Entity.Producto.Producto;
import com.example.fotocopiadora_backend.Enum.TipoProducto;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProductoMapper {
    // --------- Mapping de precios fotocopias ---------
    public PrecioFotocopia toEntity(PrecioFotocopiaDto dto) {
        PrecioFotocopia entity = new PrecioFotocopia();
        entity.setMinimo(dto.getMinimo());
        entity.setPrecioUnitario(dto.getPrecioUnitario());
        return entity;
    }

    public PrecioFotocopiaDto toDto(PrecioFotocopia entity) {
        PrecioFotocopiaDto dto = new PrecioFotocopiaDto();
        dto.setMinimo(entity.getMinimo());
        dto.setPrecioUnitario(entity.getPrecioUnitario());
        return dto;
    }

    public List<PrecioFotocopia> toPrecioEntityList(List<PrecioFotocopiaDto> dtos) {
        return dtos.stream().map(this::toEntity).collect(Collectors.toList());
    }

    public List<PrecioFotocopiaDto> toPrecioDtoList(List<PrecioFotocopia> entities) {
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    // --------- Mapping de RequestDto a Entity ---------

    public Producto fromProductoVentaDto(ProductoVentaRequestDto dto) {
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setStock(dto.getStock());
        producto.setPrecioUnitario(dto.getPrecioUnitario());
        producto.setTipoProducto(TipoProducto.PRODUCTO_VENTA);
        return producto;
    }

    public Producto fromInsumoDto(InsumoRequestDto dto) {
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setStock(dto.getStock());
        producto.setTipoProducto(TipoProducto.INSUMO);
        return producto;
    }

    public Producto fromFotocopiaDto(FotocopiaRequestDto dto) {
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setTipoProducto(TipoProducto.FOTOCOPIA);
        validarPreciosFotocopiaUnicos(dto.getPreciosFotocopia());
        List<PrecioFotocopia> precios = toPrecioEntityList(dto.getPreciosFotocopia());
        for (PrecioFotocopia precio : precios) {
            precio.setProducto(producto); // <- Establecer la relación inversa
        }
        producto.setPreciosFotocopia(precios);
        return producto;
    }

    public Producto toEntity(ProductoRequestDto dto) {
        if (dto instanceof ProductoVentaRequestDto ventaDto) {
            return fromProductoVentaDto(ventaDto);
        } else if (dto instanceof InsumoRequestDto insumoDto) {
            return fromInsumoDto(insumoDto);
        } else if (dto instanceof FotocopiaRequestDto fotocopiaDto) {
            return fromFotocopiaDto(fotocopiaDto);
        } else {
            throw new IllegalArgumentException("Tipo de DTO no soportado");
        }
    }

    // --------- Mapping de Entity a ResponseDto ---------

    public ProductoVentaResponseDto toProductoVentaDto(Producto producto) {
        ProductoVentaResponseDto dto = new ProductoVentaResponseDto();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setStock(producto.getStock());
        dto.setPrecioUnitario(producto.getPrecioUnitario());
        dto.setTipoProducto(producto.getTipoProducto());
        dto.setSoftDelete(producto.isSoftDelete());
        return dto;
    }

    public InsumoResponseDto toInsumoDto(Producto producto) {
        InsumoResponseDto dto = new InsumoResponseDto();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setStock(producto.getStock());
        dto.setTipoProducto(producto.getTipoProducto());
        dto.setSoftDelete(producto.isSoftDelete());
        return dto;
    }

    public FotocopiaResponseDto toFotocopiaDto(Producto producto) {
        FotocopiaResponseDto dto = new FotocopiaResponseDto();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setTipoProducto(producto.getTipoProducto());
        dto.setPreciosFotocopia(toPrecioDtoList(producto.getPreciosFotocopia()));
        dto.setSoftDelete(producto.isSoftDelete());
        return dto;
    }

    public ProductoResponseDto toDto(Producto producto) {
        return switch (producto.getTipoProducto()) {
            case PRODUCTO_VENTA -> toProductoVentaDto(producto);
            case INSUMO -> toInsumoDto(producto);
            case FOTOCOPIA -> toFotocopiaDto(producto);
        };
    }

    public List<ProductoResponseDto> toResponseDtoList(List<Producto> productos) {
        return productos.stream().map(this::toDto).collect(Collectors.toList());
    }

    // --------- Update Methods ---------

    public void updateProductoFromDto(ProductoRequestDto dto, Producto producto) {
        producto.setNombre(dto.getNombre());
        if (dto instanceof ProductoVentaRequestDto ventaDto) {
            producto.setPrecioUnitario(ventaDto.getPrecioUnitario());
            producto.setStock(ventaDto.getStock());
        } else if (dto instanceof InsumoRequestDto insumoDto) {
            producto.setStock(insumoDto.getStock());
        } else if (dto instanceof FotocopiaRequestDto fotocopiaDto) {
            validarPreciosFotocopiaUnicos(fotocopiaDto.getPreciosFotocopia());
            List<PrecioFotocopia> precios = toPrecioEntityList(fotocopiaDto.getPreciosFotocopia());
            for (PrecioFotocopia precio : precios) {
                precio.setProducto(producto); // relación inversa
            }
            producto.getPreciosFotocopia().clear();
            producto.getPreciosFotocopia().addAll(precios);
        }
    }

    private void validarPreciosFotocopiaUnicos(List<PrecioFotocopiaDto> preciosFotocopia) {
        Set<Integer> minimos = new HashSet<>();
        for (PrecioFotocopiaDto precio : preciosFotocopia) {
            if (!minimos.add(precio.getMinimo())) {
                throw new IllegalArgumentException("No puede haber dos precios con el mismo valor mínimo para una fotocopia");
            }
        }
    }
}
