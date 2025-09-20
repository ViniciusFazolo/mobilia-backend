package com.example.mobilia.services;

import org.springframework.stereotype.Service;

import com.example.mobilia.domain.Imovel;
import com.example.mobilia.dto.imovel.ImovelRequestDTO;
import com.example.mobilia.dto.imovel.ImovelResponseDTO;
import com.example.mobilia.mapper.ImovelMapper;
import com.example.mobilia.repository.ImovelRepository;

@Service
public class ImovelService extends CrudServiceImpl<Imovel, ImovelRequestDTO, ImovelResponseDTO, Long> {

    public ImovelService(ImovelRepository repository, ImovelMapper mapper) {
        super(repository, mapper);
    }
}

