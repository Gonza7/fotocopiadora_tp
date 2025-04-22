package com.example.fotocopiadora_backend.Service;

import com.example.fotocopiadora_backend.Dto.UsuarioRequestDto;
import com.example.fotocopiadora_backend.Dto.UsuarioResponseDto;
import com.example.fotocopiadora_backend.Entity.Usuario;
import com.example.fotocopiadora_backend.Exception.ContraseñaIncorrectaException;
import com.example.fotocopiadora_backend.Exception.UsuarioNoEncontradoException;
import com.example.fotocopiadora_backend.Exception.UsuarioYaExisteException;
import com.example.fotocopiadora_backend.Mapper.UsuarioMapper;
import com.example.fotocopiadora_backend.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
            throw new UsuarioYaExisteException("El nombre ya está registrado");
        }
        System.out.println("DTO: " + usuarioRequestDto.getNombre() + " - " + usuarioRequestDto.getContraseña());
        Usuario usuario = usuarioMapper.toEntity(usuarioRequestDto);
        System.out.println("Entidad: " + usuario.getNombre() + " - " + usuario.getContraseña());

        usuarioRepository.save(usuario);
        return usuarioMapper.toDto(usuario);
    }
    @Override
    public UsuarioResponseDto updateUsuario(Long id, UsuarioRequestDto usuarioRequestDto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));
        if (usuarioRepository.existsByNombre(usuarioRequestDto.getNombre()) && !usuario.getNombre().equals(usuarioRequestDto.getNombre())) {
            throw new UsuarioYaExisteException("El nombre ya está registrado");
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
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));
        return usuarioMapper.toDto(usuario);
    }
    @Override
    public UsuarioResponseDto deleteUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));
        usuarioRepository.deleteById(id);
        return usuarioMapper.toDto(usuario);
    }
    @Override
    public UsuarioResponseDto login(UsuarioRequestDto usuarioRequestDto) {
        Usuario usuario = usuarioRepository.findByNombre(usuarioRequestDto.getNombre())
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));
        if(!usuario.getContraseña().equals(usuarioRequestDto.getContraseña())){
            throw new ContraseñaIncorrectaException("Contraseña incorrecta");
        }
        return usuarioMapper.toDto(usuario);
    }
}
