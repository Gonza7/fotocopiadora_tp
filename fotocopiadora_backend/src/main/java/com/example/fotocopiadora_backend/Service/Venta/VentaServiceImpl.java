package com.example.fotocopiadora_backend.Service.Venta;

import com.example.fotocopiadora_backend.Dto.Venta.Request.VentaRequestDto;
import com.example.fotocopiadora_backend.Dto.Venta.Response.VentaResponseDto;
import com.example.fotocopiadora_backend.Entity.Venta.Venta;
import com.example.fotocopiadora_backend.Mapper.Venta.VentaMapper;
import com.example.fotocopiadora_backend.Repository.Venta.VentaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService{
    private final VentaRepository ventaRepository;
    private final VentaMapper ventaMapper;

    @Override
    public VentaResponseDto createVenta(VentaRequestDto ventaRequestDto) {
        Venta venta = ventaMapper.toEntity(ventaRequestDto);
        ventaRepository.save(venta);
        return ventaMapper.toDto(venta);
    }

    @Override
    public VentaResponseDto getVenta(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada"));
        return ventaMapper.toDto(venta);
    }

    @Override
    public Page<VentaResponseDto> listVenta(Pageable pageable) {
        Page<Venta> ventas = ventaRepository.findAll(pageable);
        if (ventas.isEmpty()) {
            throw new EntityNotFoundException("No se encontraron ventas");
        }
        return ventas.map(ventaMapper::toDto);
    }

    @Override
    public Page<VentaResponseDto> listVentaActive(Pageable pageable) {
        Page<Venta> ventas = ventaRepository.findAllBySoftDeleteFalse(pageable);
        if (ventas.isEmpty()) {
            throw new EntityNotFoundException("No se encontraron ventas activas");
        }
        return ventas.map(ventaMapper::toDto);
    }

    @Override
    public VentaResponseDto updateVenta(Long id, VentaRequestDto ventaRequestDto) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada"));
        ventaMapper.updateEntityFromDto(venta, ventaRequestDto);
        ventaRepository.save(venta);
        return ventaMapper.toDto(venta);
    }

    @Override
    public VentaResponseDto deleteVenta(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada"));
        if (venta.isSoftDelete()) {
            throw new IllegalArgumentException("La venta ya está eliminada");
        }
        venta.setSoftDelete(true);
        ventaRepository.save(venta);
        return ventaMapper.toDto(venta);
    }

    @Override
    public VentaResponseDto activateVenta(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada"));
        if (!venta.isSoftDelete()) {
            throw new IllegalArgumentException("La venta no está eliminada");
        }
        venta.setSoftDelete(false);
        ventaRepository.save(venta);
        return ventaMapper.toDto(venta);
    }
}
