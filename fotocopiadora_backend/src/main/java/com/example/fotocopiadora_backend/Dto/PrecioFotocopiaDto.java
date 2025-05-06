package com.example.fotocopiadora_backend.Dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrecioFotocopiaDto {
    private Long id;

    @NotNull
    @Min(1)
    private Integer minimo;

    @NotNull
    @DecimalMin("0.01")
    private float precioUnitario;
}
