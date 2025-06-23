package com.example.fotocopiadora_backend.Dto.Compra.Response;

import com.example.fotocopiadora_backend.Enum.EstadoCompra;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleCompraResponseDto {
    private String producto;
    private int cantidad;
    private EstadoCompra estadoCompra;
    private boolean softDelete;

}
