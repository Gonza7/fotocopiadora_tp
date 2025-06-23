package com.example.fotocopiadora_backend.Dto.Compra.Response;

import com.example.fotocopiadora_backend.Enum.EstadoCompra;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompraResponseDto {
    private Long id;
    private LocalDateTime fechaPago;
    private String proveedor;
    private List<DetalleCompraResponseDto> detalleCompra;
    private double monto;
    private EstadoCompra estadoCompra;
    private boolean softDelete;
}
