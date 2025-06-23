package com.example.fotocopiadora_backend.Entity.Compra;

import com.example.fotocopiadora_backend.Enum.EstadoCompra;
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
@Table(name = "COMPRA")
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "FECHA_PAGO", nullable = false)
    private LocalDateTime fechaPago;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleCompra> detalleCompra;

    @Column(name = "MONTO", nullable = false)
    private double monto;

    @Column(name = "ESTADO", nullable = false)
    private EstadoCompra estadoCompra;

    @Column(name = "SOFT_DELETE", nullable = false)
    private boolean softDelete = false;

    @PrePersist
    public void prePersist() {
        this.fechaPago = LocalDateTime.now();
    }
}
