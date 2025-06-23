package com.example.fotocopiadora_backend.Repository.Compra;


import com.example.fotocopiadora_backend.Entity.Compra.Compra;
import com.example.fotocopiadora_backend.Enum.EstadoCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
    Page<Compra> findAllBySoftDeleteFalse(Pageable pageable);
    Page<Compra> findAllBySoftDeleteFalseAndEstadoCompraEqualsIgnoreCase(EstadoCompra estadoCompra, Pageable pageable);
}
