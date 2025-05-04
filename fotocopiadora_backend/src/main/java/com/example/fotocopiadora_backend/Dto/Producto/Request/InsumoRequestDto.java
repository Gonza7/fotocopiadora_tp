package com.example.fotocopiadora_backend.Dto.Producto.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class InsumoRequestDto {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
}
