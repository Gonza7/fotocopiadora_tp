package com.example.fotocopiadora_backend.Service.Usuario;

import com.example.fotocopiadora_backend.Dto.Usuario.UsuarioRequestDto;
import com.example.fotocopiadora_backend.Dto.Usuario.UsuarioResponseDto;
import com.example.fotocopiadora_backend.Entity.Usuario.Usuario;
import com.example.fotocopiadora_backend.Mapper.Usuario.UsuarioMapper;
import com.example.fotocopiadora_backend.Repository.Usuario.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }
    @Override
    public UsuarioResponseDto createUsuario(UsuarioRequestDto usuarioRequestDto) {
        if (usuarioRepository.existsByNombre(usuarioRequestDto.getNombre())) {
            throw new IllegalArgumentException("El nombre ya está registrado");
        }
        Usuario usuario = usuarioMapper.toEntity(usuarioRequestDto);
        usuarioRepository.save(usuario);
        return usuarioMapper.toDto(usuario);
    }
    @Override
    public UsuarioResponseDto updateUsuario(Long id, UsuarioRequestDto usuarioRequestDto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        if (usuarioRepository.existsByNombre(usuarioRequestDto.getNombre()) && !usuario.getNombre().equals(usuarioRequestDto.getNombre())) {
            throw new IllegalArgumentException("El nombre ya está registrado");
        }
        usuarioMapper.updateUsuarioFromDto(usuarioRequestDto, usuario);
        usuarioRepository.save(usuario);
        return usuarioMapper.toDto(usuario);
    }
    @Override
    public List<UsuarioResponseDto> listUsuario() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarioMapper.toDtoList(usuarios);
    }
    @Override
    public UsuarioResponseDto getUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        return usuarioMapper.toDto(usuario);
    }
    @Override
    public UsuarioResponseDto deleteUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        usuarioRepository.deleteById(id);
        return usuarioMapper.toDto(usuario);
    }
    @Override
    public UsuarioResponseDto login(UsuarioRequestDto usuarioRequestDto) {
        Usuario usuario = usuarioRepository.findByNombre(usuarioRequestDto.getNombre())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        if(!usuario.getContraseña().equals(usuarioRequestDto.getContraseña())){
            throw new IllegalArgumentException("Contraseña incorrecta");
        }
        return usuarioMapper.toDto(usuario);
    }
}
