package com.example.fotocopiadora_backend.Controller.Compra;

import com.example.fotocopiadora_backend.Dto.Compra.Request.CompraRequestDto;
import com.example.fotocopiadora_backend.Dto.Compra.Response.CompraResponseDto;
import com.example.fotocopiadora_backend.Service.Compra.CompraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/compra")
@RequiredArgsConstructor
public class CompraController {
    private final CompraService compraService;

    @PostMapping
    public ResponseEntity<CompraResponseDto> createCompra(@Valid @RequestBody CompraRequestDto compraRequestDto) {
        CompraResponseDto compraResponseDto = compraService.createCompra(compraRequestDto);
        return ResponseEntity.status(201).body(compraResponseDto);
    }

    @GetMapping
    public ResponseEntity<Page<CompraResponseDto>> listCompra(@PageableDefault(size = 10, sort = "fechaPago", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CompraResponseDto> compras = compraService.listCompra(pageable);
        return ResponseEntity.ok(compras);
    }

    @GetMapping("/active")
    public ResponseEntity<Page<CompraResponseDto>> listCompraActive(@PageableDefault(size = 10, sort = "fechaPago", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CompraResponseDto> compras = compraService.listCompraActive(pageable);
        return ResponseEntity.ok(compras);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraResponseDto> getCompra(@PathVariable Long id) {
        CompraResponseDto compraResponseDto = compraService.getCompra(id);
        return ResponseEntity.ok(compraResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompraResponseDto> updateCompra(@PathVariable Long id, @Valid @RequestBody CompraRequestDto compraRequestDto) {
        CompraResponseDto compraResponseDto = compraService.updateCompra(id, compraRequestDto);
        return ResponseEntity.ok(compraResponseDto);
    }

    @PutMapping("delete/{id}")
    public ResponseEntity<CompraResponseDto> deleteCompra(@PathVariable Long id) {
        CompraResponseDto compraResponseDto = compraService.deleteCompra(id);
        return ResponseEntity.ok(compraResponseDto);
    }

    @PutMapping("activate/{id}")
    public ResponseEntity<CompraResponseDto> activateCompra(@PathVariable Long id) {
        CompraResponseDto compraResponseDto = compraService.activateCompra(id);
        return ResponseEntity.ok(compraResponseDto);
    }

    @PutMapping("status/{idCompra}/{idDetalle}")
    public ResponseEntity<CompraResponseDto> updateEstadoCompra(@PathVariable Long idCompra, @PathVariable Long idDetalle) {
        CompraResponseDto compraResponseDto = compraService.updateEstadoCompra(idCompra, idDetalle);
        return ResponseEntity.ok(compraResponseDto);
    }

}
