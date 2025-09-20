package com.example.mobilia.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.mobilia.domain.Morador;
import com.example.mobilia.dto.morador.MoradorRequestDTO;
import com.example.mobilia.dto.morador.MoradorResponseDTO;

@Mapper(componentModel = "spring")
public interface MoradorMapper extends IGenericMapper<MoradorRequestDTO, MoradorResponseDTO, Morador>{

    @Override
    @Mapping(source = "unidade", target = "unidade.id")
    @Mapping(source = "imovel", target = "imovel.id")
    Morador toEntity(MoradorRequestDTO obj);

}
