package com.example.fotocopiadora_backend.Dto.Venta.Response;

import com.example.fotocopiadora_backend.Enum.FormaPago;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VentaResponseDto {
    private Long id;
    private FormaPago formaPago;
    private LocalDateTime fechaPago;
    private double precioTotal;
    private List<DetalleVentaResponseDto> detallesVenta;
}
