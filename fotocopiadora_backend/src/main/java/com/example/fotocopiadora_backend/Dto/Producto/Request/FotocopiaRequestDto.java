package com.example.fotocopiadora_backend.Dto.Producto.Request;

import com.example.fotocopiadora_backend.Dto.Producto.PrecioFotocopiaDto;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class FotocopiaRequestDto {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotEmpty(message = "La lista de precios no puede estar vacía")
    private List<PrecioFotocopiaDto> precios;
}
