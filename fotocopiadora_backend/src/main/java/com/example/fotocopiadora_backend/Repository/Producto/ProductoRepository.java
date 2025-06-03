package com.example.fotocopiadora_backend.Repository.Producto;

import com.example.fotocopiadora_backend.Entity.Producto.Producto;
import com.example.fotocopiadora_backend.Enum.TipoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findBySoftDeleteFalse();
    List<Producto> findByNombreContainingIgnoreCaseAndSoftDeleteFalse(String nombre);
    List<Producto> findByTipoProducto(TipoProducto tipoProducto);
    List<Producto> findByTipoProductoAndSoftDeleteFalse(TipoProducto tipoProducto);
    boolean existsByNombreIgnoreCaseAndTipoProducto(String nombre, TipoProducto tipoProducto);
    boolean existsByNombreIgnoreCaseAndTipoProductoAndIdNot(String nombre, TipoProducto tipoProducto, Long id);
}
