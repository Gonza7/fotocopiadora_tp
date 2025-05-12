package com.example.fotocopiadora_backend.Dto.Producto.Request;

import com.example.fotocopiadora_backend.Enum.TipoProducto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public abstract class ProductoRequestDto {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
    
    @NotNull(message = "El tipo de producto no puede estar vacío")
    private TipoProducto tipoProducto;
}