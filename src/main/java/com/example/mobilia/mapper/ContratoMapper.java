package com.example.mobilia.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.mobilia.domain.Contrato;
import com.example.mobilia.dto.contrato.ContratoRequestDTO;
import com.example.mobilia.dto.contrato.ContratoResponseDTO;

@Mapper(componentModel = "spring")
public interface ContratoMapper extends IGenericMapper<ContratoRequestDTO, ContratoResponseDTO, Contrato> {
    
    @Override
    @Mapping(source = "imovel", target = "imovel.id")
    @Mapping(source = "unidade", target = "unidade.id")
    @Mapping(source = "morador", target = "morador.id")
    @Mapping(source = "user", target = "locador.id")
    @Mapping(target = "dtCadastro", ignore = true)
    @Mapping(target = "pdfContrato", ignore = true)
    @Mapping(target = "status", ignore = true)
    Contrato toEntity(ContratoRequestDTO obj);
    
    @Override
    @Mapping(source = "locador", target = "userId")
    @Mapping(source = "morador", target = "moradorId")
    @Mapping(source = "unidade", target = "unidadeId")
    @Mapping(source = "imovel", target = "imovelId")
    ContratoResponseDTO toDto(Contrato obj);
}
