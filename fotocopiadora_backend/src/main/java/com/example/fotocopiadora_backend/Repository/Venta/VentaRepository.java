package com.example.fotocopiadora_backend.Repository.Venta;

import com.example.fotocopiadora_backend.Entity.Venta.Venta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    Page<Venta> findAllBySoftDeleteFalse(Pageable pageable);
}
