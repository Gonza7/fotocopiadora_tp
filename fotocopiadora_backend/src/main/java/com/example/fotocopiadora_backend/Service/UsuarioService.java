package com.example.fotocopiadora_backend.Service;

import com.example.fotocopiadora_backend.Dto.UsuarioRequestDto;
import com.example.fotocopiadora_backend.Dto.UsuarioResponseDto;

import java.util.List;

public interface UsuarioService {
    UsuarioResponseDto createUsuario(UsuarioRequestDto usuarioRequestDto);
    UsuarioResponseDto updateUsuario(Long id, UsuarioRequestDto usuarioRequestDto);
    List<UsuarioResponseDto> listUsuario();
    UsuarioResponseDto getUsuario(Long id);
    UsuarioResponseDto deleteUsuario(Long id);
    UsuarioResponseDto login(UsuarioRequestDto usuarioRequestDto);
}
