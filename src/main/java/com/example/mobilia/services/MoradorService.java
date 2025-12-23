package com.example.mobilia.services;

import org.springframework.stereotype.Service;

import com.example.mobilia.domain.Morador;
import com.example.mobilia.domain.Unidade;
import com.example.mobilia.dto.morador.MoradorRequestDTO;
import com.example.mobilia.dto.morador.MoradorResponseDTO;
import com.example.mobilia.mapper.MoradorMapper;
import com.example.mobilia.repository.MoradorRepository;
import com.example.mobilia.repository.UnidadeRepository;

import jakarta.transaction.Transactional;

@Service
public class MoradorService extends CrudServiceImpl<Morador, MoradorRequestDTO, MoradorResponseDTO, Long> {

    private final MoradorRepository moradorRepository;
    private final UnidadeRepository unidadeRepository;
    private final MoradorMapper moradorMapper;

    public MoradorService(MoradorRepository repository, MoradorMapper mapper, UnidadeRepository unidadeRepository) {
        super(repository, mapper);
        this.moradorRepository = repository;
        this.moradorMapper = mapper;
        this.unidadeRepository = unidadeRepository;
    }

    @Override
    @Transactional
    public MoradorResponseDTO create(MoradorRequestDTO dto) {
        Unidade unidade = unidadeRepository.findById(dto.unidade())
            .orElseThrow(() -> new RuntimeException("Unidade não encontrada"));
        
        Morador morador = moradorMapper.toEntity(dto);
        morador.setUnidade(unidade);
        // O imovel é obtido automaticamente da unidade
        morador.setImovel(unidade.getImovel());
        
        morador = moradorRepository.save(morador);
        return moradorMapper.toDto(morador);
    }

    @Override
    @Transactional
    public MoradorResponseDTO update(Long id, MoradorRequestDTO dto) {
        Morador morador = moradorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Morador não encontrado"));
        
        // Atualizar dados básicos
        morador.setNome(dto.nome());
        morador.setEmail(dto.email());
        morador.setTelefone(dto.telefone());
        morador.setCpf(dto.cpf());
        morador.setRg(dto.rg());
        morador.setAtivo(dto.ativo());
        morador.setDtVencimento(dto.dtVencimento());
        morador.setDtInicio(dto.dtInicio());
        morador.setDtFim(dto.dtFim());
        
        // Atualizar unidade se fornecida
        if (dto.unidade() != null) {
            Unidade unidade = unidadeRepository.findById(dto.unidade())
                .orElseThrow(() -> new RuntimeException("Unidade não encontrada"));
            morador.setUnidade(unidade);
            // O imovel é atualizado automaticamente a partir da nova unidade
            morador.setImovel(unidade.getImovel());
        }
        
        morador = moradorRepository.save(morador);
        return moradorMapper.toDto(morador);
    }

}
