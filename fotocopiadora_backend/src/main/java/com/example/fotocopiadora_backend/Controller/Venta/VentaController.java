package com.example.fotocopiadora_backend.Controller.Venta;

import com.example.fotocopiadora_backend.Dto.Venta.Request.VentaRequestDto;
import com.example.fotocopiadora_backend.Dto.Venta.Response.VentaResponseDto;
import com.example.fotocopiadora_backend.Service.Venta.VentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/venta")
@RequiredArgsConstructor
public class VentaController {
    private final VentaService ventaService;

    @PostMapping
    public ResponseEntity<VentaResponseDto> createVenta(@Valid @RequestBody VentaRequestDto ventaRequestDto) {
        VentaResponseDto ventaResponseDto = ventaService.createVenta(ventaRequestDto);
        return ResponseEntity.status(201).body(ventaResponseDto);
    }

    @GetMapping
    public ResponseEntity<Page<VentaResponseDto>> listVenta(@PageableDefault(size = 10, sort = "fechaPago", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<VentaResponseDto> ventas = ventaService.listVenta(pageable);
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/active")
    public ResponseEntity<Page<VentaResponseDto>> listVentaActive(@PageableDefault(size = 10, sort = "fechaPago", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<VentaResponseDto> ventas = ventaService.listVentaActive(pageable);
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaResponseDto> getVenta(@PathVariable Long id) {
        VentaResponseDto ventaResponseDto = ventaService.getVenta(id);
        return ResponseEntity.ok(ventaResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VentaResponseDto> updateVenta(@PathVariable Long id, @Valid @RequestBody VentaRequestDto ventaRequestDto) {
        VentaResponseDto ventaResponseDto = ventaService.updateVenta(id, ventaRequestDto);
        return ResponseEntity.ok(ventaResponseDto);
    }

    @PutMapping("delete/{id}")
    public ResponseEntity<VentaResponseDto> deleteVenta(@PathVariable Long id) {
        VentaResponseDto ventaResponseDto = ventaService.deleteVenta(id);
        return ResponseEntity.ok(ventaResponseDto);
    }

    @PutMapping("activate/{id}")
    public ResponseEntity<VentaResponseDto> activateVenta(@PathVariable Long id) {
        VentaResponseDto ventaResponseDto = ventaService.activateVenta(id);
        return ResponseEntity.ok(ventaResponseDto);
    }
}
