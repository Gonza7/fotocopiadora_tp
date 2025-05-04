package com.example.fotocopiadora_backend.Entity.Producto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "PRODUCTO_VENTA")
public class Kiosco extends ProductoVenta{
    @Column(name = "PRECIO_UNITARIO", nullable = false)
    private float precioUnitario;
}
