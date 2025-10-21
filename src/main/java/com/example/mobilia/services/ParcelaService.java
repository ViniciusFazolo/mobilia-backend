package com.example.mobilia.services;

import org.springframework.stereotype.Service;

import com.example.mobilia.domain.Parcela;
import com.example.mobilia.dto.parcela.ParcelaRequestDTO;
import com.example.mobilia.dto.parcela.ParcelaResponseDTO;
import com.example.mobilia.mapper.ParcelaMapper;
import com.example.mobilia.repository.ParcelaRepository;

@Service
public class ParcelaService extends CrudServiceImpl<Parcela, ParcelaRequestDTO, ParcelaResponseDTO, Long> {

    public ParcelaService(ParcelaRepository repository, ParcelaMapper mapper) {
        super(repository, mapper);
    }
}
