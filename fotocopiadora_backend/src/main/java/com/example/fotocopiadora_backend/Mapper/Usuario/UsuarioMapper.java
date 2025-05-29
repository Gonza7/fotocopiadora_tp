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
    @Mapping(target = "id", ignore = true) // Ignorar el ID al crear un nuevo usuario
    Usuario toEntity(UsuarioRequestDto usuarioRequestDto);
    @Mapping(source = "id", target = "id") // Mapear el ID del usuario
    UsuarioResponseDto toDto(Usuario usuario);
    List<UsuarioResponseDto> toDtoList(List<Usuario> usuarios);
    void updateUsuarioFromDto(UsuarioRequestDto usuarioRequestDto, @MappingTarget Usuario usuario);
}
