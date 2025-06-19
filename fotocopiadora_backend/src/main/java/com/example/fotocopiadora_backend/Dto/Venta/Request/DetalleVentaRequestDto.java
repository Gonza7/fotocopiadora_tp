package com.example.fotocopiadora_backend.Dto.Venta.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleVentaRequestDto {
    @NotNull(message = "El id del producto no puede estar vacio")
    private Long idProducto;

    @Min(1)
    private int cantidad;
}
