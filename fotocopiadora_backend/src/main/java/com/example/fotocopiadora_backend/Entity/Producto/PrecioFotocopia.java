package com.example.fotocopiadora_backend.Entity.Producto;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PRECIO_FOTOCOPIA")

public class PrecioFotocopia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "MINIMO", nullable = false)
    private Integer minimo;

    @Column(name = "PRECIO_UNITARIO", nullable = false)
    private double precioUnitario;

    @ManyToOne
    @JoinColumn(name = "ID_PRODUCTO", nullable = false)
    private Producto producto;
}
