package com.example.fotocopiadora_backend.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FotocopiaResponseDto {
    private Long id;

    private String nombre;

    private Boolean softDelete;

    private List<PrecioFotocopiaDto> precios;
}
