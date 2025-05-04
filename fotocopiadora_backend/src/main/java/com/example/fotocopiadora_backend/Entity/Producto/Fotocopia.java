package com.example.fotocopiadora_backend.Entity.Producto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "FOTOCOPIA")
public class Fotocopia extends ProductoVenta {
    @OneToMany(mappedBy = "fotocopia", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(name = "PRECIOS", nullable = false)
    private List<PrecioFotocopia> precios = new ArrayList<>();
}
