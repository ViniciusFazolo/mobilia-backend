package com.example.mobilia.services;

import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.example.mobilia.domain.Contrato;
import com.example.mobilia.domain.Parcela;
import com.example.mobilia.domain.User;
import com.example.mobilia.dto.parcela.ParcelaRequestDTO;
import com.example.mobilia.dto.parcela.ParcelaResponseDTO;
import com.example.mobilia.mapper.ParcelaMapper;
import com.example.mobilia.repository.ContratoRepository;
import com.example.mobilia.repository.ParcelaRepository;
import com.example.mobilia.utils.SecurityUtils;

import jakarta.transaction.Transactional;

@Service
public class ParcelaService extends CrudServiceImpl<Parcela, ParcelaRequestDTO, ParcelaResponseDTO, Long> {

    private final ParcelaRepository parcelaRepository;
    private final ContratoRepository contratoRepository;
    private final ParcelaMapper parcelaMapper;
    private final SecurityUtils securityUtils;

    public ParcelaService(ParcelaRepository repository, ParcelaMapper mapper, ContratoRepository contratoRepository, SecurityUtils securityUtils) {
        super(repository, mapper);
        this.parcelaRepository = repository;
        this.parcelaMapper = mapper;
        this.contratoRepository = contratoRepository;
        this.securityUtils = securityUtils;
    }

    @Override
    public Iterable<ParcelaResponseDTO> getAll() {
        User currentUser = securityUtils.getCurrentUser();
        Iterable<Parcela> entities = parcelaRepository.findAllByContrato_Locador(currentUser);

        return StreamSupport.stream(entities.spliterator(), false)
                .map(parcelaMapper::toDto)
                .toList();
    }
    
    @Override
    public ParcelaResponseDTO getById(Long id) {
        User currentUser = securityUtils.getCurrentUser();
        Parcela entity = parcelaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Parcela não encontrada"));
        
        // Verifica se a parcela pertence a um contrato do usuário
        if (!entity.getContrato().getLocador().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Você não tem permissão para acessar esta parcela");
        }
        
        return parcelaMapper.toDto(entity);
    }

    @Override
    @Transactional
    public ParcelaResponseDTO update(Long id, ParcelaRequestDTO dto) {
        User currentUser = securityUtils.getCurrentUser();
        Parcela parcela = parcelaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Parcela não encontrada"));
        
        // Verifica se a parcela pertence a um contrato do usuário
        if (!parcela.getContrato().getLocador().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Você não tem permissão para atualizar esta parcela");
        }
        
        // Atualizar dados básicos
        parcela.setNumeroParcela(dto.numeroParcela());
        parcela.setDataVencimento(dto.dataVencimento());
        parcela.setValor(dto.valor());
        parcela.setDataPagamento(dto.dataPagamento());
        parcela.setStatus(dto.status());
        
        // Buscar e atualizar o contrato se fornecido
        if (dto.contrato() != null) {
            Contrato contrato = contratoRepository.findById(dto.contrato())
                .orElseThrow(() -> new RuntimeException("Contrato não encontrado"));
            
            // Verifica se o novo contrato pertence ao usuário
            if (!contrato.getLocador().getId().equals(currentUser.getId())) {
                throw new RuntimeException("Você não tem permissão para usar este contrato");
            }
            
            parcela.setContrato(contrato);
        }
        
        parcela = parcelaRepository.save(parcela);
        return parcelaMapper.toDto(parcela);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User currentUser = securityUtils.getCurrentUser();
        Parcela entity = parcelaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Parcela não encontrada"));
        
        // Verifica se a parcela pertence a um contrato do usuário
        if (!entity.getContrato().getLocador().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Você não tem permissão para excluir esta parcela");
        }
        
        parcelaRepository.deleteById(id);
    }
}
