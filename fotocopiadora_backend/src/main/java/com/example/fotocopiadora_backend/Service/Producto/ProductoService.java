package com.example.fotocopiadora_backend.Service.Producto;

import com.example.fotocopiadora_backend.Dto.Producto.Request.ProductoRequestDto;
import com.example.fotocopiadora_backend.Dto.Producto.Response.ProductoResponseDto;
import com.example.fotocopiadora_backend.Enum.TipoProducto;

import java.util.List;

public interface ProductoService {
    ProductoResponseDto createProducto(ProductoRequestDto productoRequestDto);
    ProductoResponseDto updateProducto(Long id, ProductoRequestDto productoRequestDto);
    ProductoResponseDto getProducto(Long id);
    ProductoResponseDto deleteProducto(Long id);
    List<ProductoResponseDto> listProducto();
    List<ProductoResponseDto> listProductoByTipo(TipoProducto tipoProducto);
    List<ProductoResponseDto> listProductoActive();
    List<ProductoResponseDto> listProductoByTipoAndActive(TipoProducto tipoProducto);
    ProductoResponseDto activateProducto(Long id);
}
