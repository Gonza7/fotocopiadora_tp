package com.example.fotocopiadora_backend.Mapper.Producto;

import com.example.fotocopiadora_backend.Dto.Producto.Request.InsumoRequestDto;
import com.example.fotocopiadora_backend.Dto.Producto.Response.InsumoResponseDto;
import com.example.fotocopiadora_backend.Entity.Producto.Insumo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InsumoMapper {
    @Mapping(target = "id", ignore = true)
    Insumo toEntity(InsumoRequestDto insumoRequestDto);
    InsumoResponseDto toDto(Insumo insumo);
    List<InsumoResponseDto> toDtoList(List<Insumo> insumos);
    void updateInsumoFromDto(InsumoRequestDto insumoRequestDto, @MappingTarget Insumo insumo);
}
