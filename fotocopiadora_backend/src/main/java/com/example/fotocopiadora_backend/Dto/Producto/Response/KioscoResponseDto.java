package com.example.fotocopiadora_backend.Dto.Producto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class KioscoResponseDto {
    private Long id;
    private String nombre;
    private float precioUnitario;
}
