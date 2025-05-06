package com.example.fotocopiadora_backend.Mapper;

import com.example.fotocopiadora_backend.Dto.FotocopiaRequestDto;
import com.example.fotocopiadora_backend.Dto.FotocopiaResponseDto;
import com.example.fotocopiadora_backend.Entity.Fotocopia;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FotocopiaMapper {
    FotocopiaResponseDto toDto(Fotocopia fotocopia);
    @Mapping(target = "id", ignore = true)
    Fotocopia toEntity(FotocopiaRequestDto fotocopiaRequestDto);
    List<FotocopiaResponseDto> toDtoList(List<Fotocopia> fotocopias);
}

