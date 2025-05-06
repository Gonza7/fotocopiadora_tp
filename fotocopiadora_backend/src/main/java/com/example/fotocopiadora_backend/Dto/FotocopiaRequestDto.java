package com.example.fotocopiadora_backend.Dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FotocopiaRequestDto {
    @NotBlank
    private String nombre;

    private Boolean softDelete = false;

    @NotEmpty
    private List<PrecioFotocopiaDto> precios;
}
