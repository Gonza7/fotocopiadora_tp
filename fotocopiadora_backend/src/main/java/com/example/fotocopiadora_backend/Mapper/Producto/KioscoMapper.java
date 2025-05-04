package com.example.fotocopiadora_backend.Mapper.Producto;

import com.example.fotocopiadora_backend.Dto.Producto.Request.KioscoRequestDto;
import com.example.fotocopiadora_backend.Dto.Producto.Response.KioscoResponseDto;
import com.example.fotocopiadora_backend.Entity.Producto.Kiosco;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface KioscoMapper {
    @Mapping(target = "id", ignore = true)
    Kiosco toEntity(KioscoRequestDto kioscoRequestDto);
    KioscoResponseDto toDto(Kiosco kiosco);
    List<KioscoResponseDto> toDtoList(List<Kiosco> kioscos);
    void updateKioscoFromDto(KioscoRequestDto kioscoRequestDto, @MappingTarget Kiosco kiosco);
}
