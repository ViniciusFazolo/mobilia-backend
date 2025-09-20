package com.example.mobilia.mapper;

import org.mapstruct.Mapper;

import com.example.mobilia.domain.Imovel;
import com.example.mobilia.dto.imovel.ImovelRequestDTO;
import com.example.mobilia.dto.imovel.ImovelResponseDTO;

@Mapper(componentModel = "spring")
public interface ImovelMapper extends IGenericMapper<ImovelRequestDTO, ImovelResponseDTO, Imovel> {

    

}
