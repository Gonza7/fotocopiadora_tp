package com.example.fotocopiadora_backend.Mapper;

import com.example.fotocopiadora_backend.Dto.UsuarioRequestDto;
import com.example.fotocopiadora_backend.Dto.UsuarioResponseDto;
import com.example.fotocopiadora_backend.Entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    Usuario toEntity(UsuarioRequestDto usuarioRequestDto);
    UsuarioResponseDto toDto(Usuario usuario);
    List<UsuarioResponseDto> toDtoList(List<Usuario> usuarios);
    void updateUsuarioFromDto(UsuarioRequestDto usuarioRequestDto, @MappingTarget Usuario usuario);
}
