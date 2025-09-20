package com.example.mobilia.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.mobilia.domain.Unidade;
import com.example.mobilia.dto.unidade.UnidadeRequestDTO;
import com.example.mobilia.dto.unidade.UnidadeResponseDTO;

@Mapper(componentModel = "spring")
public interface UnidadeMapper extends IGenericMapper<UnidadeRequestDTO, UnidadeResponseDTO, Unidade> {

    @Override
    @Mapping(source = "imovel", target = "imovel.id")
    Unidade toEntity(UnidadeRequestDTO obj);
}
