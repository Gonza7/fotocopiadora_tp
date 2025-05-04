package com.example.fotocopiadora_backend.Dto.Producto;

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
    @NotNull(message = "El minimo no puede estar vacío")
    private Integer minimo;

    @NotNull(message = "El precio unitario no puede estar vacío")
    private float precioUnitario;
}
