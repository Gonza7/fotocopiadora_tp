package com.example.fotocopiadora_backend.Entity.Producto;

import com.example.fotocopiadora_backend.Enum.TipoProducto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "PRODUCTO")
public abstract class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOMBRE", nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_PRODUCTO", nullable = false)
    private TipoProducto tipoProducto;

    @Column(name = "PRECIO_UNITARIO")
    private float precioUnitario;

    @Column(name = "PRECIOS_FOTOCOPIA")
    private List<PrecioFotocopia> precios;

    @Column(name = "SOFT_DELETE", nullable = false)
    private boolean softDelete = false;
}