package com.example.fotocopiadora_backend.Dto.Producto.Request;

import com.example.fotocopiadora_backend.Enum.TipoProducto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class InsumoRequestDto extends ProductoRequestDto {
    @Min(value=0, message = "El stock no puede ser negativo")
    private int stock;
}
