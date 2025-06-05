package com.example.fotocopiadora_backend.Dto.Producto.Request;

import com.example.fotocopiadora_backend.Enum.TipoProducto;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ProductoVentaRequestDto extends ProductoRequestDto {
    @Min(value = 0, message = "El stock no puede ser negativo")
    private int stock;

    @Min(value = 0, message = "El precio unitario no puede ser negativo")
    private double precioUnitario;

}
