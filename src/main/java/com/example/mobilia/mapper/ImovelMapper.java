package com.example.mobilia.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.mobilia.domain.Imovel;
import com.example.mobilia.dto.imovel.ImovelRequestDTO;
import com.example.mobilia.dto.imovel.ImovelResponseDTO;

@Mapper(componentModel = "spring")
public interface ImovelMapper extends IGenericMapper<ImovelRequestDTO, ImovelResponseDTO, Imovel> {

    @Override
    @Mapping(target = "imagem", ignore = true)
    @Mapping(target = "dtCadastro", ignore = true)
    Imovel toEntity(ImovelRequestDTO obj);

    @Override
    @Mapping(source = "imagem", target = "imagens")
    ImovelResponseDTO toDto(Imovel obj);

}
