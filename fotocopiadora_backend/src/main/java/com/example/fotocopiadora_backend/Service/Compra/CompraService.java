package com.example.fotocopiadora_backend.Service.Compra;

import com.example.fotocopiadora_backend.Dto.Compra.Request.CompraRequestDto;
import com.example.fotocopiadora_backend.Dto.Compra.Response.CompraResponseDto;
import com.example.fotocopiadora_backend.Enum.EstadoCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompraService {
    public CompraResponseDto createCompra(CompraRequestDto compraRequestDto);
    public CompraResponseDto getCompra(Long id);
    public CompraResponseDto updateCompra(Long id, CompraRequestDto compraRequestDto);
    public CompraResponseDto deleteCompra(Long id);
    public CompraResponseDto activateCompra(Long id);
    public CompraResponseDto updateEstadoCompra(Long idCompra, Long idDetalle);
    public Page<CompraResponseDto> listCompra(Pageable pageable);
    public Page<CompraResponseDto> listCompraActive(Pageable pageable);
    public Page<CompraResponseDto> listCompraByEstadoCompra(EstadoCompra estadoCompra, Pageable pageable);
}
