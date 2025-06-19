package com.example.fotocopiadora_backend.Dto.Venta.Request;

import com.example.fotocopiadora_backend.Enum.FormaPago;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VentaRequestDto {
    @NotNull(message = "La forma de pago no puede estar vacia")
    private FormaPago formaPago;

    @NotNull(message = "La lista de detalles no puede estar vacia")
    @Valid
    private List<DetalleVentaRequestDto> detallesVenta;
}
