package com.example.fotocopiadora_backend.Service.Venta;

import com.example.fotocopiadora_backend.Dto.Venta.Request.VentaRequestDto;
import com.example.fotocopiadora_backend.Dto.Venta.Response.VentaResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VentaService {
    VentaResponseDto createVenta(VentaRequestDto ventaRequestDto);
    VentaResponseDto getVenta(Long id);
    Page<VentaResponseDto> listVenta(Pageable pageable);
    Page<VentaResponseDto> listVentaActive(Pageable pageable);
    VentaResponseDto updateVenta(Long id, VentaRequestDto ventaRequestDto);
    VentaResponseDto deleteVenta(Long id);
    VentaResponseDto activateVenta(Long id);

}
