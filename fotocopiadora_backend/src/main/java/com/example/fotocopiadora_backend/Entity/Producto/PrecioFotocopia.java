package com.example.fotocopiadora_backend.Entity.Producto;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "PRECIO_UNITARIO")

public class PrecioFotocopia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "MINIMO", nullable = false)
    private Integer minimo;

    @Column(name = "PRECIO_UNITARIO", nullable = false)
    private float precioUnitario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FOTOCOPIA_ID", referencedColumnName = "ID", nullable = false)
    private Producto fotocopia;
}
