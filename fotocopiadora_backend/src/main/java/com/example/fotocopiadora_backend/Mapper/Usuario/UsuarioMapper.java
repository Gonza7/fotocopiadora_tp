package com.example.fotocopiadora_backend.Mapper.Usuario;

import com.example.fotocopiadora_backend.Dto.Usuario.UsuarioRequestDto;
import com.example.fotocopiadora_backend.Dto.Usuario.UsuarioResponseDto;
import com.example.fotocopiadora_backend.Entity.Usuario.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    @Mapping(target = "id", ignore = true)
    Usuario toEntity(UsuarioRequestDto usuarioRequestDto);
    UsuarioResponseDto toDto(Usuario usuario);
    List<UsuarioResponseDto> toDtoList(List<Usuario> usuarios);
    void updateUsuarioFromDto(UsuarioRequestDto usuarioRequestDto, @MappingTarget Usuario usuario);
}
