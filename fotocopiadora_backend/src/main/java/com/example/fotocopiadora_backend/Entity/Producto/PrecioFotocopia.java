package com.example.fotocopiadora_backend.Entity.Producto;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Embeddable
@Table(name = "PRECIO_FOTOCOPIA")

public class PrecioFotocopia {

    @Column(name = "MINIMO", nullable = false)
    private Integer minimo;

    @Column(name = "PRECIO_UNITARIO", nullable = false)
    private double precioUnitario;

}
