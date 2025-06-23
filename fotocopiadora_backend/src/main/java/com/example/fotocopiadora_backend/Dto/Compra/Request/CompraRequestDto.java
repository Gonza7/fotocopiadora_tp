package com.example.fotocopiadora_backend.Dto.Compra.Request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompraRequestDto {
    @NotNull(message = "El proveedor no puede ser nulo")
    private String proveedor;

    @NotNull(message = "La lista de detalles no puede estar vacia")
    @Valid
    private List<DetalleCompraRequestDto> detalleCompra;

    @Min(value = 0, message = "El monto no puede ser negativo")
    private double monto;
}
