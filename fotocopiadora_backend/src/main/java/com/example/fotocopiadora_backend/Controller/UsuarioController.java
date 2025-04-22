package com.example.fotocopiadora_backend.Controller;

import com.example.fotocopiadora_backend.Dto.UsuarioRequestDto;
import com.example.fotocopiadora_backend.Dto.UsuarioResponseDto;
import com.example.fotocopiadora_backend.Service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/usuario")
public class UsuarioController {
    @Autowired
    private final UsuarioService usuarioService;
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> createUsuario(@Valid @RequestBody UsuarioRequestDto usuarioRequestDto) {
        UsuarioResponseDto usuarioResponseDto = usuarioService.createUsuario(usuarioRequestDto);
        return ResponseEntity.status(201).body(usuarioResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> listUsuarios() {
        return ResponseEntity.ok(usuarioService.listUsuario());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> getUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.getUsuario(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> updateUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDto usuarioRequestDto) {
        UsuarioResponseDto usuarioResponseDto = usuarioService.updateUsuario(id, usuarioRequestDto);
        return ResponseEntity.ok(usuarioResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> deleteUsuario(@PathVariable Long id) {
        UsuarioResponseDto usuarioResponseDto = usuarioService.deleteUsuario(id);
        return ResponseEntity.ok(usuarioResponseDto);
    }
}
