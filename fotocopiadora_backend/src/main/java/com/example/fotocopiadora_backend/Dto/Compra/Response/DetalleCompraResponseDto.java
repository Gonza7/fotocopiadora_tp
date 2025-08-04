package com.example.fotocopiadora_backend.Dto.Compra.Response;

import com.example.fotocopiadora_backend.Enum.EstadoCompra;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleCompraResponseDto {
    private Long idProducto;
    private String nombreProducto;
    private int cantidad;
    private EstadoCompra estadoCompra;
}
