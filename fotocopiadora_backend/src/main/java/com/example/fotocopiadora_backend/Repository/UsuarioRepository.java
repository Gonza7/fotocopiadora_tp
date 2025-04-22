package com.example.fotocopiadora_backend.Repository;

import com.example.fotocopiadora_backend.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}
