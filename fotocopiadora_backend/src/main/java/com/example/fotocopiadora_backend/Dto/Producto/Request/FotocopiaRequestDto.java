package com.example.fotocopiadora_backend.Dto.Producto.Request;

import com.example.fotocopiadora_backend.Dto.Producto.PrecioFotocopiaDto;
import com.example.fotocopiadora_backend.Enum.TipoProducto;
import jakarta.validation.Valid;
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
public class FotocopiaRequestDto extends ProductoRequestDto {
    @NotEmpty(message = "La lista de precios no puede estar vacía")
    @Size(min = 1, message = "Debe haber al menos un precio definido")
    @Valid
    private List<PrecioFotocopiaDto> preciosFotocopia;

}
