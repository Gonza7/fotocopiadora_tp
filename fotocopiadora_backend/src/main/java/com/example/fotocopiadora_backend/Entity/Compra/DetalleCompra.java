package com.example.fotocopiadora_backend.Entity.Compra;

import com.example.fotocopiadora_backend.Entity.Producto.Producto;
import com.example.fotocopiadora_backend.Entity.Venta.Venta;
import com.example.fotocopiadora_backend.Enum.EstadoCompra;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "DETALLE_COMPRA")
public class DetalleCompra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_PRODUCTO", nullable = false)
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "ID_COMPRA", nullable = false)
    private Compra compra;

    @Column(name = "CANTIDAD", nullable = false)
    private int cantidad;

    @Column(name = "ESTADO_COMPRA", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoCompra estadoCompra;
}
