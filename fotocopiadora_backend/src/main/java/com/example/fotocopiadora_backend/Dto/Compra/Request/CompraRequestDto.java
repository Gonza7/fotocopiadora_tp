package com.example.fotocopiadora_backend.Dto.Compra.Request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompraRequestDto {
    @NotBlank(message = "El proveedor no puede estar vacío")
    private String proveedor;

    @NotNull(message = "La lista de detalles no puede estar vacia")
    @Valid
    private List<DetalleCompraRequestDto> detalleCompra;

    @Min(value = 0, message = "El monto no puede ser negativo")
    private double monto;
}
