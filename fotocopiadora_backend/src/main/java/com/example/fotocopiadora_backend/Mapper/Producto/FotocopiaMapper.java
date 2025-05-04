package com.example.fotocopiadora_backend.Mapper.Producto;

import com.example.fotocopiadora_backend.Dto.Producto.Request.FotocopiaRequestDto;
import com.example.fotocopiadora_backend.Dto.Producto.Response.FotocopiaResponseDto;
import com.example.fotocopiadora_backend.Entity.Producto.Fotocopia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FotocopiaMapper {
    @Mapping(target = "id", ignore = true)
    Fotocopia toEntity(FotocopiaRequestDto fotocopiaRequestDto);
    FotocopiaResponseDto toDto(Fotocopia fotocopia);
    List<FotocopiaResponseDto> toDtoList(List<Fotocopia> fotocopias);
    void UpdateFotocopiaFromDto(FotocopiaRequestDto fotocopiaRequestDto, @MappingTarget Fotocopia fotocopia);
}
