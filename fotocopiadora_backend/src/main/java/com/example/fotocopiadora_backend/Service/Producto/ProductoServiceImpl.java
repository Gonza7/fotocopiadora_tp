package com.example.fotocopiadora_backend.Service.Producto;

import com.example.fotocopiadora_backend.Dto.Producto.Request.ProductoRequestDto;
import com.example.fotocopiadora_backend.Dto.Producto.Response.ProductoResponseDto;
import com.example.fotocopiadora_backend.Entity.Producto.Producto;
import com.example.fotocopiadora_backend.Enum.TipoProducto;
import com.example.fotocopiadora_backend.Mapper.Producto.ProductoMapper;
import com.example.fotocopiadora_backend.Repository.Producto.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {
    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    public ProductoResponseDto createProducto(ProductoRequestDto productoRequestDto) {
        if(productoRepository.existsByNombreIgnoreCaseAndTipoProducto(
                productoRequestDto.getNombre(), productoRequestDto.getTipoProducto())) {
            throw new IllegalArgumentException("Ya existe un producto con el mismo nombre y tipo");
        }
        Producto producto = productoMapper.toEntity(productoRequestDto);
        productoRepository.save(producto);
        return productoMapper.toDto(producto);
    }
    public ProductoResponseDto updateProducto(Long id, ProductoRequestDto productoRequestDto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
        if (!producto.getTipoProducto().equals(productoRequestDto.getTipoProducto())) {
            throw new IllegalArgumentException("No se puede cambiar el tipo de producto en la actualización");
        }
        if(productoRepository.existsByNombreIgnoreCaseAndTipoProductoAndIdNot(
                productoRequestDto.getNombre(), productoRequestDto.getTipoProducto(), id)) {
            throw new IllegalArgumentException("Ya existe un producto con el mismo nombre y tipo");
        }
        productoMapper.updateProductoFromDto(productoRequestDto, producto);
        productoRepository.save(producto);
        return productoMapper.toDto(producto);
    }
    public ProductoResponseDto getProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
        return productoMapper.toDto(producto);
    }
    public ProductoResponseDto deleteProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
        if (producto.isSoftDelete()) {
            throw new IllegalArgumentException("El producto ya está eliminado");
        }
        producto.setSoftDelete(true);
        productoRepository.save(producto);
        return productoMapper.toDto(producto);
    }
    public List<ProductoResponseDto> listProducto() {
        List<Producto> productos = productoRepository.findAll();
        if (productos.isEmpty()) {
            throw new EntityNotFoundException("No se encontraron productos");
        }
        return productoMapper.toResponseDtoList(productos);
    }
    public List<ProductoResponseDto> listProductoByTipo(TipoProducto tipoProducto) {
        List<Producto> productos = productoRepository.findByTipoProductoAndSoftDeleteFalse(tipoProducto);
        if (productos.isEmpty()) {
            throw new EntityNotFoundException("No se encontraron productos del tipo: " + tipoProducto);
        }
        return productoMapper.toResponseDtoList(productos);
    }
    public List<ProductoResponseDto> listProductoActive() {
        List<Producto> productos = productoRepository.findBySoftDeleteFalse();
        if (productos.isEmpty()) {
            throw new EntityNotFoundException("No se encontraron productos disponibles");
        }
        return productoMapper.toResponseDtoList(productos);
    }
    public List<ProductoResponseDto> listProductoByTipoAndActive(TipoProducto tipoProducto) {
        List<Producto> productos = productoRepository.findByTipoProductoAndSoftDeleteFalse(tipoProducto);
        if (productos.isEmpty()) {
            throw new EntityNotFoundException("No se encontraron productos del tipo: " + tipoProducto + " disponibles");
        }
        return productoMapper.toResponseDtoList(productos);
    }
    public ProductoResponseDto activateProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
        if (!producto.isSoftDelete()) {
            throw new IllegalArgumentException("El producto ya está activo");
        }
        producto.setSoftDelete(false);
        productoRepository.save(producto);
        return productoMapper.toDto(producto);
    }
}
