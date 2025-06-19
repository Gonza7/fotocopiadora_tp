package com.example.fotocopiadora_backend.Entity.Venta;

import com.example.fotocopiadora_backend.Entity.Producto.Producto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "DETALLE_VENTA")
public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_PRODUCTO", nullable = false)
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "ID_VENTA", nullable = false)
    private Venta venta;

    @Column(name = "CANTIDAD", nullable = false)
    private int cantidad;

    @Column(name = "PRECIO_UNITARIO", nullable = false)
    private double precioUnitario;
}
