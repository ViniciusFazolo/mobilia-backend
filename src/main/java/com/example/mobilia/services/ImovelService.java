package com.example.mobilia.services;

import org.springframework.stereotype.Service;

import com.example.mobilia.domain.Imovel;
import com.example.mobilia.dto.imovel.ImovelRequestDTO;
import com.example.mobilia.dto.imovel.ImovelResponseDTO;
import com.example.mobilia.mapper.ImovelMapper;
import com.example.mobilia.repository.ImovelRepository;

import jakarta.transaction.Transactional;

@Service
public class ImovelService extends CrudServiceImpl<Imovel, ImovelRequestDTO, ImovelResponseDTO, Long> {

    private final ImovelRepository repository;
    private final ImovelMapper mapper;
    private final CloudinaryService cloudinaryService;

    public ImovelService(ImovelRepository repository, ImovelMapper mapper, CloudinaryService cloudinaryService) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    @Transactional
    public ImovelResponseDTO create(ImovelRequestDTO request) {
        try {
            Imovel entity = mapper.toEntity(request);

            if (request.imagens() != null && !request.imagens().isEmpty()) {
                String url = cloudinaryService.uploadFile(request.imagens());
                entity.setImagem(url);
            }

            entity = repository.save(entity);
            return mapper.toDto(entity);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar Imóvel com imagem", e);
        }
    }

    @Override
    @Transactional
    public ImovelResponseDTO update(Long id, ImovelRequestDTO request) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Imóvel não encontrado");
        }

        try {
            // Busca a entidade existente do banco (preserva a imagem)
            Imovel entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Imóvel não encontrado"));

            // Atualiza os campos da entidade existente
            entity.setNome(request.nome());
            entity.setCep(request.cep());
            entity.setEstado(request.estado());
            entity.setCidade(request.cidade());
            entity.setBairro(request.bairro());
            entity.setRua(request.rua());
            entity.setNumero(request.numero());
            entity.setComplemento(request.complemento());
            entity.setAtivo(request.ativo());

            // Só atualiza imagem se houver uma nova imagem no request
            if (request.imagens() != null && !request.imagens().isEmpty()) {
                String url = cloudinaryService.uploadFile(request.imagens());
                entity.setImagem(url);
            }
            // Se request.imagens() for null ou vazio, mantém a imagem existente

            entity = repository.save(entity);
            return mapper.toDto(entity);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar Imóvel com imagem", e);
        }
    }
}

