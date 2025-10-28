package com.example.mobilia.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.mobilia.domain.Parcela;
import com.example.mobilia.dto.parcela.ParcelaRequestDTO;
import com.example.mobilia.dto.parcela.ParcelaResponseDTO;

@Mapper(componentModel = "spring")
public interface ParcelaMapper extends IGenericMapper<ParcelaRequestDTO, ParcelaResponseDTO, Parcela> {

    @Override
    @Mapping(source = "contrato", target = "contrato.id")
    Parcela toEntity(ParcelaRequestDTO obj);
}
