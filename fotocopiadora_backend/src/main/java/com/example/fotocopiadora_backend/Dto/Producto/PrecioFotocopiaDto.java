package com.example.fotocopiadora_backend.Dto.Producto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PrecioFotocopiaDto {
    @Min(value = 1, message = "El mínimo de copias debe ser al menos 1")
    private Integer minimo;

    @Min(value = 0, message = "El precio unitario no puede ser negativo")
    private double precioUnitario;
}
