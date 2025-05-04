package com.example.fotocopiadora_backend.Dto.Producto.Response;

import com.example.fotocopiadora_backend.Dto.Producto.PrecioFotocopiaDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class FotocopiaResponseDto {
    private Long id;
    private String nombre;
    private List<PrecioFotocopiaDto> precios;
}
