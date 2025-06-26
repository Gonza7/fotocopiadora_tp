package com.example.fotocopiadora_backend.Service.Compra;

import com.example.fotocopiadora_backend.Dto.Compra.Request.CompraRequestDto;
import com.example.fotocopiadora_backend.Dto.Compra.Response.CompraResponseDto;
import com.example.fotocopiadora_backend.Entity.Compra.Compra;
import com.example.fotocopiadora_backend.Entity.Compra.DetalleCompra;
import com.example.fotocopiadora_backend.Enum.EstadoCompra;
import com.example.fotocopiadora_backend.Mapper.Compra.CompraMapper;
import com.example.fotocopiadora_backend.Repository.Compra.CompraRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompraServiceImpl implements CompraService {
    private final CompraRepository compraRepository;
    private final CompraMapper compraMapper;

    @Override
    public CompraResponseDto createCompra(CompraRequestDto compraRequestDto) {
        Compra compra = compraMapper.toEntity(compraRequestDto);
        compraRepository.save(compra);
        return compraMapper.toDto(compra);
    }

    @Override
    public CompraResponseDto getCompra(Long id) {
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Compra no encontrada"));
        return compraMapper.toDto(compra);
    }

    @Override
    public CompraResponseDto updateCompra(Long id, CompraRequestDto compraRequestDto) {
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Compra no encontrada"));
        compraMapper.updateEntityFromDto(compra, compraRequestDto);
        compraRepository.save(compra);
        return compraMapper.toDto(compra);
    }

    @Override
    public CompraResponseDto deleteCompra(Long id) {
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Compra no encontrada"));
        if(compra.isSoftDelete()) {
            throw new IllegalArgumentException("La compra ya está eliminada");
        }
        compra.setSoftDelete(true);
        compraRepository.save(compra);
        return compraMapper.toDto(compra);
    }

    @Override
    public CompraResponseDto activateCompra(Long id) {
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Compra no encontrada"));
        if (!compra.isSoftDelete()) {
            throw new IllegalArgumentException("La compra ya está activa");
        }
        compra.setSoftDelete(false);
        compraRepository.save(compra);
        return compraMapper.toDto(compra);
    }

    @Override
    @Transactional
    public CompraResponseDto updateEstadoCompra(Long idCompra, Long idProducto) {
        Compra compra = compraRepository.findById(idCompra)
                .orElseThrow(() -> new EntityNotFoundException("Compra no encontrada con ID: " + idCompra));

        // Buscar el detalle dentro de la compra
        DetalleCompra detalle = compra.getDetalleCompra().stream()
                .filter(d -> d.getProducto().getId().equals(idProducto))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + idProducto + "compra:" + idCompra));

        // Validar estado actual
        if (detalle.getEstadoCompra() == EstadoCompra.FINALIZADO) {
            throw new IllegalArgumentException("Este detalle ya fue finalizado");
        }

        // Cambiar el estado del detalle
        detalle.setEstadoCompra(EstadoCompra.FINALIZADO);

        // Verificar si todos los detalles están finalizados
        boolean todosFinalizados = compra.getDetalleCompra().stream()
                .allMatch(d -> d.getEstadoCompra() == EstadoCompra.FINALIZADO);

        if (todosFinalizados) {
            compra.setEstadoCompra(EstadoCompra.FINALIZADO);
        } else {
            compra.setEstadoCompra(EstadoCompra.PENDIENTE);
        }

        // Guardar la compra con el cambio en cascada
        compraRepository.save(compra);

        // Retornar el DTO de respuesta
        return compraMapper.toDto(compra);
    }

    @Override
    public Page<CompraResponseDto> listCompra(Pageable pageable) {
        Page<Compra> compras = compraRepository.findAll(pageable);
        if (compras.isEmpty()) {
            throw new EntityNotFoundException("No se encontraron compras");
        }
        return compras.map(compraMapper::toDto);
    }

    @Override
    public Page<CompraResponseDto> listCompraActive(Pageable pageable) {
        Page<Compra> compras = compraRepository.findAllBySoftDeleteFalse(pageable);
        if (compras.isEmpty()) {
            throw new EntityNotFoundException("No se encontraron compras activas");
        }
        return compras.map(compraMapper::toDto);
    }

    @Override
    public Page<CompraResponseDto> listCompraByEstadoCompra(EstadoCompra estadoCompra, Pageable pageable) {
        Page<Compra> compras = compraRepository.findAllBySoftDeleteFalseAndEstadoCompraEqualsIgnoreCase(estadoCompra, pageable);
        if (compras.isEmpty()) {
            throw new EntityNotFoundException("No se encontraron compras con el estado: " + estadoCompra);
        }
        return compras.map(compraMapper::toDto);
    }
}
