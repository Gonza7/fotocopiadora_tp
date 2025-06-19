package com.example.fotocopiadora_backend.Dto.Venta.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleVentaResponseDto {
    private String producto;
    private int cantidad;
    private double precioUnitario;
}
