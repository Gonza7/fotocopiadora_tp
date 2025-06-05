package com.example.fotocopiadora_backend.Dto.Producto.Response;

import com.example.fotocopiadora_backend.Enum.TipoProducto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class InsumoResponseDto extends ProductoResponseDto {
    private int stock;
}
