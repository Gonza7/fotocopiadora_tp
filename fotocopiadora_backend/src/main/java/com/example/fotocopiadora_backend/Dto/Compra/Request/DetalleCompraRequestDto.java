package com.example.fotocopiadora_backend.Dto.Compra.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleCompraRequestDto {
    @NotNull(message = "El id del producto no puede estar vacio")
    private Long idProducto;

    @Min(value = 1, message = "La cantidad no puede ser menor a 1")
    private int cantidad;
}
