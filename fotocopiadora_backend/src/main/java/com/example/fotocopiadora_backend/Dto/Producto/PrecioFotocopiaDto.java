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
    @NotNull(message = "El mínimo de copias no puede ser nulo")
    @Min(value = 1, message = "El mínimo de copias debe ser al menos 1")
    private Integer minimo;

    @NotNull(message = "El precio unitario no puede ser nulo")
    @Min(value = 1, message = "El precio unitario no puede ser menor a 1")
    private Double precioUnitario;
}
