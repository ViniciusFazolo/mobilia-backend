package com.example.mobilia.services;

import org.springframework.stereotype.Service;

import com.example.mobilia.domain.Morador;
import com.example.mobilia.dto.morador.MoradorRequestDTO;
import com.example.mobilia.dto.morador.MoradorResponseDTO;
import com.example.mobilia.mapper.MoradorMapper;
import com.example.mobilia.repository.MoradorRepository;

@Service
public class MoradorService extends CrudServiceImpl<Morador, MoradorRequestDTO, MoradorResponseDTO, Long> {

    public MoradorService(MoradorRepository repository, MoradorMapper mapper) {
        super(repository, mapper);
    }

}
