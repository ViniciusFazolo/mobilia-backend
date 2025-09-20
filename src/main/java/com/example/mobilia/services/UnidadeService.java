package com.example.mobilia.services;

import org.springframework.stereotype.Service;

import com.example.mobilia.domain.Unidade;
import com.example.mobilia.dto.unidade.UnidadeRequestDTO;
import com.example.mobilia.dto.unidade.UnidadeResponseDTO;
import com.example.mobilia.repository.UnidadeRepository;
import com.example.mobilia.mapper.UnidadeMapper;

@Service
public class UnidadeService extends CrudServiceImpl<Unidade, UnidadeRequestDTO, UnidadeResponseDTO, Long> {

    public UnidadeService(UnidadeRepository repository, UnidadeMapper mapper) {
        super(repository, mapper);
    }

}
