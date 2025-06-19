package com.example.fotocopiadora_backend.Entity.Venta;

import com.example.fotocopiadora_backend.Enum.FormaPago;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "VENTA")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "FORMA_PAGO", nullable = false)
    private FormaPago formaPago;

    @Column(name = "FECHA_PAGO", nullable = false)
    private LocalDateTime fechaPago;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalleVenta;

    @Column(name = "PRECIO_TOTAL", nullable = false)
    private double precioTotal;

    @Column(name = "SOFT_DELETE", nullable = false)
    private boolean softDelete = false;

    @PrePersist
    public void prePersist() {
        this.fechaPago = LocalDateTime.now();
    }
}
