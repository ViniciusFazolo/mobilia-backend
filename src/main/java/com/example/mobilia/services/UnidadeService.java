package com.example.mobilia.services;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.example.mobilia.domain.Unidade;
import com.example.mobilia.dto.unidade.UnidadeRequestDTO;
import com.example.mobilia.dto.unidade.UnidadeResponseDTO;
import com.example.mobilia.repository.UnidadeRepository;

import jakarta.transaction.Transactional;

import com.example.mobilia.mapper.UnidadeMapper;

@Service
public class UnidadeService extends CrudServiceImpl<Unidade, UnidadeRequestDTO, UnidadeResponseDTO, Long> {

    private final CloudinaryService cloudinaryService;
    private final UnidadeMapper mapper;
    private final UnidadeRepository repository;
    public UnidadeService(UnidadeRepository repository, UnidadeMapper mapper, CloudinaryService cloudinaryService) {
        super(repository, mapper);
        this.cloudinaryService = cloudinaryService;
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public UnidadeResponseDTO create(UnidadeRequestDTO request) {
        try {
            Unidade entity = mapper.toEntity(request);

            if (request.imagens() != null && !request.imagens().isEmpty()) {
                for (var file : request.imagens()) {
                    String url = cloudinaryService.uploadFile(file);
                    entity.getImagens().add(url);
                }
            }

            entity = repository.save(entity);
            return mapper.toDto(entity);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar Unidade com imagens", e);
        }
    }

    @Override
    @Transactional
    public UnidadeResponseDTO update(Long id, UnidadeRequestDTO request) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Unidade não encontrada");
        }

        // Busca a entidade existente do banco (preserva as imagens)
        Unidade entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Unidade não encontrada"));

        // Atualiza os campos da entidade existente
        entity.setIdentificacao(request.identificacao());
        entity.setBloco(request.bloco());
        entity.setComplemento(request.complemento());
        entity.setValorAluguel(request.valorAluguel());
        entity.setAreaTotal(request.areaTotal());
        entity.setQtdBanheiro(request.qtdBanheiro());
        entity.setQtdQuarto(request.qtdQuarto());
        entity.setQtdSuite(request.qtdSuite());
        entity.setQtdGaragem(request.qtdGaragem());
        entity.setQtdSala(request.qtdSala());
        entity.setCozinha(request.cozinha());
        entity.setAreaServico(request.areaServico());
        entity.setStatus(request.status());
        entity.setDescricao(request.descricao());
        entity.setAtivo(request.ativo());
        
        // Atualiza o imóvel se necessário
        if (request.imovel() != null) {
            // Aqui você precisa buscar o Imovel pelo ID
            // entity.setImovel(imovelRepository.findById(request.imovel()).orElseThrow());
        }

        // Só atualiza imagens se houver novas imagens no request
        if (request.imagens() != null && !request.imagens().isEmpty()) {
            entity.getImagens().clear();
            try {
                for (var file : request.imagens()) {
                    String url = cloudinaryService.uploadFile(file);
                    entity.getImagens().add(url);
                }
            } catch (IOException e) {
                throw new RuntimeException("Erro ao fazer upload das imagens", e);
            }
        }
        // Se request.imagens() for null ou vazio, mantém as imagens existentes
        
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    
}


