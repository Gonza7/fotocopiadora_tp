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
    @NotBlank(message = "El stock no puede estar vacío")
    private int stock;

    @NotNull(message = "El precio unitario no puede estar vacío")
    private double precioUnitario;

}
