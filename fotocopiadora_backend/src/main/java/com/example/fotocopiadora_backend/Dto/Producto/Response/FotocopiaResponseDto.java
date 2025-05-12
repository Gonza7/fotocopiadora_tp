package com.example.fotocopiadora_backend.Dto.Producto.Response;

import com.example.fotocopiadora_backend.Dto.Producto.PrecioFotocopiaDto;
import com.example.fotocopiadora_backend.Enum.TipoProducto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class FotocopiaResponseDto extends ProductoResponseDto {
    private List<PrecioFotocopiaDto> precios;

}
