package com.example.fotocopiadora_backend.Dto.Producto.Request;

import com.example.fotocopiadora_backend.Enum.TipoProducto;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "tipoProducto",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ProductoVentaRequestDto.class, name = "PRODUCTO_VENTA"),
        @JsonSubTypes.Type(value = InsumoRequestDto.class, name = "INSUMO"),
        @JsonSubTypes.Type(value = FotocopiaRequestDto.class, name = "FOTOCOPIA"),
})

public abstract class ProductoRequestDto {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotNull(message = "El tipo de producto no puede estar vacío")
    private TipoProducto tipoProducto;
}