package com.example.fotocopiadora_backend.Repository.Producto;

import com.example.fotocopiadora_backend.Entity.Producto.Producto;
import com.example.fotocopiadora_backend.Enum.TipoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findBySoftDeleteFalse();
    Optional<Producto> findByIdAndSoftDeleteFalse(Long id);
    List<Producto> findByTipoProducto(TipoProducto tipoProducto);
    List<Producto> findByTipoProductoAndSoftDeleteFalse(TipoProducto tipoProducto);
    boolean existsByNombreIgnoreCaseAndTipoProducto(String nombre, TipoProducto tipoProducto);
    boolean existsByNombreIgnoreCaseAndTipoProductoAndIdNot(String nombre, TipoProducto tipoProducto, Long id);
}
