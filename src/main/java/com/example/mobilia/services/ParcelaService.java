package com.example.mobilia.services;

import org.springframework.stereotype.Service;

import com.example.mobilia.domain.Contrato;
import com.example.mobilia.domain.Parcela;
import com.example.mobilia.dto.parcela.ParcelaRequestDTO;
import com.example.mobilia.dto.parcela.ParcelaResponseDTO;
import com.example.mobilia.mapper.ParcelaMapper;
import com.example.mobilia.repository.ContratoRepository;
import com.example.mobilia.repository.ParcelaRepository;

import jakarta.transaction.Transactional;

@Service
public class ParcelaService extends CrudServiceImpl<Parcela, ParcelaRequestDTO, ParcelaResponseDTO, Long> {

    private final ParcelaRepository parcelaRepository;
    private final ContratoRepository contratoRepository;
    private final ParcelaMapper parcelaMapper;

    public ParcelaService(ParcelaRepository repository, ParcelaMapper mapper, ContratoRepository contratoRepository) {
        super(repository, mapper);
        this.parcelaRepository = repository;
        this.parcelaMapper = mapper;
        this.contratoRepository = contratoRepository;
    }

    @Override
    @Transactional
    public ParcelaResponseDTO update(Long id, ParcelaRequestDTO dto) {
        Parcela parcela = parcelaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Parcela não encontrada"));
        
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
            parcela.setContrato(contrato);
        }
        
        parcela = parcelaRepository.save(parcela);
        return parcelaMapper.toDto(parcela);
    }
}
