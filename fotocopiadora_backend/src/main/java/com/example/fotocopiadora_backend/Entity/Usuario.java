package com.example.fotocopiadora_backend.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "Usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOMBRE", nullable = false)
    private String nombre;

    @Column(name = "CONTRASEÑA", nullable = false)
    private String contraseña;
}
