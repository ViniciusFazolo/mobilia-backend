package com.example.mobilia.services;

import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.example.mobilia.domain.Morador;
import com.example.mobilia.domain.Unidade;
import com.example.mobilia.domain.User;
import com.example.mobilia.dto.morador.MoradorRequestDTO;
import com.example.mobilia.dto.morador.MoradorResponseDTO;
import com.example.mobilia.mapper.MoradorMapper;
import com.example.mobilia.repository.MoradorRepository;
import com.example.mobilia.repository.UnidadeRepository;
import com.example.mobilia.utils.SecurityUtils;

import jakarta.transaction.Transactional;

@Service
public class MoradorService extends CrudServiceImpl<Morador, MoradorRequestDTO, MoradorResponseDTO, Long> {

    private final MoradorRepository moradorRepository;
    private final UnidadeRepository unidadeRepository;
    private final MoradorMapper moradorMapper;
    private final SecurityUtils securityUtils;

    public MoradorService(MoradorRepository repository, MoradorMapper mapper, UnidadeRepository unidadeRepository, SecurityUtils securityUtils) {
        super(repository, mapper);
        this.moradorRepository = repository;
        this.moradorMapper = mapper;
        this.unidadeRepository = unidadeRepository;
        this.securityUtils = securityUtils;
    }

    @Override
    @Transactional
    public MoradorResponseDTO create(MoradorRequestDTO dto) {
        User currentUser = securityUtils.getCurrentUser();
        Unidade unidade = unidadeRepository.findById(dto.unidade())
            .orElseThrow(() -> new RuntimeException("Unidade não encontrada"));
        
        // Verifica se a unidade pertence ao usuário
        if (!unidade.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Você não tem permissão para criar morador nesta unidade");
        }
        
        Morador morador = moradorMapper.toEntity(dto);
        morador.setUnidade(unidade);
        // O imovel é obtido automaticamente da unidade
        morador.setImovel(unidade.getImovel());
        morador.setUser(currentUser);
        
        morador = moradorRepository.save(morador);
        return moradorMapper.toDto(morador);
    }

    @Override
    @Transactional
    public MoradorResponseDTO update(Long id, MoradorRequestDTO dto) {
        User currentUser = securityUtils.getCurrentUser();
        Morador morador = moradorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Morador não encontrado"));
        
        // Verifica se o morador pertence ao usuário
        if (!morador.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Você não tem permissão para atualizar este morador");
        }
        
        // Atualizar dados básicos
        morador.setNome(dto.nome());
        morador.setEmail(dto.email());
        morador.setTelefone(dto.telefone());
        morador.setCpf(dto.cpf());
        morador.setRg(dto.rg());
        morador.setAtivo(dto.ativo());
        morador.setRua(dto.rua());
        morador.setBairro(dto.bairro());
        morador.setCep(dto.cep());
        morador.setCidade(dto.cidade());
        morador.setEstado(dto.estado());
        
        // Atualizar unidade se fornecida
        if (dto.unidade() != null) {
            Unidade unidade = unidadeRepository.findById(dto.unidade())
                .orElseThrow(() -> new RuntimeException("Unidade não encontrada"));
            
            // Verifica se a nova unidade pertence ao usuário
            if (!unidade.getUser().getId().equals(currentUser.getId())) {
                throw new RuntimeException("Você não tem permissão para usar esta unidade");
            }
            
            morador.setUnidade(unidade);
            // O imovel é atualizado automaticamente a partir da nova unidade
            morador.setImovel(unidade.getImovel());
        }
        
        morador = moradorRepository.save(morador);
        return moradorMapper.toDto(morador);
    }

    @Override
    public Iterable<MoradorResponseDTO> getAll() {
        User currentUser = securityUtils.getCurrentUser();
        Iterable<Morador> entities = moradorRepository.findAllByUser(currentUser);

        return StreamSupport.stream(entities.spliterator(), false)
                .map(moradorMapper::toDto)
                .toList();
    }
    
    @Override
    public MoradorResponseDTO getById(Long id) {
        User currentUser = securityUtils.getCurrentUser();
        Morador entity = moradorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Morador não encontrado"));
        
        if (!entity.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Você não tem permissão para acessar este morador");
        }
        
        return moradorMapper.toDto(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User currentUser = securityUtils.getCurrentUser();
        Morador entity = moradorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Morador não encontrado"));
        
        if (!entity.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Você não tem permissão para excluir este morador");
        }
        
        moradorRepository.deleteById(id);
    }

}
