package com.example.mobilia.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.mobilia.domain.Contrato;
import com.example.mobilia.dto.contrato.ContratoRequestDTO;
import com.example.mobilia.dto.contrato.ContratoResponseDTO;

@Mapper(componentModel = "spring")
public interface ContratoMapper extends IGenericMapper<ContratoRequestDTO, ContratoResponseDTO, Contrato> {
    
    @Override
    @Mapping(source = "morador", target = "morador.id")
    @Mapping(source = "user", target = "locador.id")
    @Mapping(target = "unidade", ignore = true)
    @Mapping(target = "imovel", ignore = true)
    @Mapping(target = "dtCadastro", ignore = true)
    @Mapping(target = "pdfContrato", ignore = true)
    @Mapping(target = "status", ignore = true)
    Contrato toEntity(ContratoRequestDTO obj);
    
    @Override
    @Mapping(source = "locador", target = "userId")
    ContratoResponseDTO toDto(Contrato obj);
}
