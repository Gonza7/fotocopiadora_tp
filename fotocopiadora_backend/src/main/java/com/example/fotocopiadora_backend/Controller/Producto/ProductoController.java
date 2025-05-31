package com.example.fotocopiadora_backend.Controller.Producto;

import com.example.fotocopiadora_backend.Dto.Producto.Request.ProductoRequestDto;
import com.example.fotocopiadora_backend.Dto.Producto.Response.ProductoResponseDto;
import com.example.fotocopiadora_backend.Enum.TipoProducto;
import com.example.fotocopiadora_backend.Service.Producto.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/producto")
@RequiredArgsConstructor
public class ProductoController {
    private final ProductoService productoService;

    @PostMapping
    public ResponseEntity<ProductoResponseDto> createProducto(@Valid @RequestBody ProductoRequestDto productoRequestDto) {
        ProductoResponseDto productoResponseDto = productoService.createProducto(productoRequestDto);
        return ResponseEntity.status(201).body(productoResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponseDto>> listProductos() {
        List<ProductoResponseDto> productos = productoService.listProducto();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDto> getProducto(@PathVariable Long id) {
        ProductoResponseDto productoResponseDto = productoService.getProducto(id);
        return ResponseEntity.ok(productoResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDto> updateProducto(@PathVariable Long id, @Valid @RequestBody ProductoRequestDto productoRequestDto) {
        ProductoResponseDto productoResponseDto = productoService.updateProducto(id, productoRequestDto);
        return ResponseEntity.ok(productoResponseDto);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<ProductoResponseDto> deleteProducto(@PathVariable Long id) {
        ProductoResponseDto productoResponseDto = productoService.deleteProducto(id);
        return ResponseEntity.ok(productoResponseDto);
    }

    @GetMapping("/type/{tipoProducto}")
    public ResponseEntity<List<ProductoResponseDto>> listProductosByTipo(@PathVariable TipoProducto tipoProducto) {
        List<ProductoResponseDto> productos = productoService.listProductoByTipo(tipoProducto);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/active")
    public ResponseEntity<List<ProductoResponseDto>> listProductosActive() {
        List<ProductoResponseDto> productos = productoService.listProductoActive();
        return ResponseEntity.ok(productos);
    }
}
