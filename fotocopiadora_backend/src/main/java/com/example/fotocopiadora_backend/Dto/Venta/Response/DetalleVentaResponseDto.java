package com.example.fotocopiadora_backend.Dto.Venta.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleVentaResponseDto {
    private Long idProducto;
    private String nombreProducto;
    private int cantidad;
    private double precioUnitario;
}
