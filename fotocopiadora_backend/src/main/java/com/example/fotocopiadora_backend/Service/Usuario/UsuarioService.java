package com.example.fotocopiadora_backend.Service.Usuario;

import com.example.fotocopiadora_backend.Dto.Usuario.UsuarioRequestDto;
import com.example.fotocopiadora_backend.Dto.Usuario.UsuarioResponseDto;

import java.util.List;

public interface UsuarioService {
    UsuarioResponseDto createUsuario(UsuarioRequestDto usuarioRequestDto);
    UsuarioResponseDto updateUsuario(Long id, UsuarioRequestDto usuarioRequestDto);
    List<UsuarioResponseDto> listUsuario();
    UsuarioResponseDto getUsuario(Long id);
    UsuarioResponseDto deleteUsuario(Long id);
    UsuarioResponseDto login(UsuarioRequestDto usuarioRequestDto);
}
