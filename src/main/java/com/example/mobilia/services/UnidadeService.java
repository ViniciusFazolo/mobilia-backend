package com.example.mobilia.services;

import java.io.IOException;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import java.util.List;

import com.example.mobilia.domain.Contrato;
import com.example.mobilia.domain.Imovel;
import com.example.mobilia.domain.Morador;
import com.example.mobilia.domain.Parcela;
import com.example.mobilia.domain.Unidade;
import com.example.mobilia.domain.User;
import com.example.mobilia.dto.unidade.UnidadeRequestDTO;
import com.example.mobilia.dto.unidade.UnidadeResponseDTO;
import com.example.mobilia.repository.ContratoRepository;
import com.example.mobilia.repository.ImovelRepository;
import com.example.mobilia.repository.MoradorRepository;
import com.example.mobilia.repository.ParcelaRepository;
import com.example.mobilia.repository.UnidadeRepository;
import com.example.mobilia.utils.SecurityUtils;

import jakarta.transaction.Transactional;

import com.example.mobilia.mapper.UnidadeMapper;

@Service
public class UnidadeService extends CrudServiceImpl<Unidade, UnidadeRequestDTO, UnidadeResponseDTO, Long> {

    private final CloudinaryService cloudinaryService;
    private final UnidadeMapper mapper;
    private final UnidadeRepository repository;
    private final ImovelRepository imovelRepository;
    private final SecurityUtils securityUtils;
    private final ContratoRepository contratoRepository;
    private final MoradorRepository moradorRepository;
    private final ParcelaRepository parcelaRepository;
    
    public UnidadeService(UnidadeRepository repository, UnidadeMapper mapper, CloudinaryService cloudinaryService, ImovelRepository imovelRepository, SecurityUtils securityUtils, ContratoRepository contratoRepository, MoradorRepository moradorRepository, ParcelaRepository parcelaRepository) {
        super(repository, mapper);
        this.cloudinaryService = cloudinaryService;
        this.mapper = mapper;
        this.repository = repository;
        this.imovelRepository = imovelRepository;
        this.securityUtils = securityUtils;
        this.contratoRepository = contratoRepository;
        this.moradorRepository = moradorRepository;
        this.parcelaRepository = parcelaRepository;
    }

    @Override
    @Transactional
    public UnidadeResponseDTO create(UnidadeRequestDTO request) {
        try {
            User currentUser = securityUtils.getCurrentUser();
            Unidade entity = mapper.toEntity(request);
            entity.setUser(currentUser);

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
        User currentUser = securityUtils.getCurrentUser();
        
        // Busca a entidade existente do banco e verifica se pertence ao usuário
        Unidade entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Unidade não encontrada"));
        
        if (!entity.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Você não tem permissão para atualizar esta unidade");
        }

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
            Imovel imovel = imovelRepository.findById(request.imovel())
                .orElseThrow(() -> new RuntimeException("Imóvel não encontrado"));
            
            // Verifica se o imóvel pertence ao usuário
            if (!imovel.getUser().getId().equals(currentUser.getId())) {
                throw new RuntimeException("Você não tem permissão para usar este imóvel");
            }
            
            entity.setImovel(imovel);
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

    @Override
    public Iterable<UnidadeResponseDTO> getAll() {
        User currentUser = securityUtils.getCurrentUser();
        Iterable<Unidade> entities = repository.findAllByUser(currentUser);

        return StreamSupport.stream(entities.spliterator(), false)
                .map(mapper::toDto)
                .toList();
    }
    
    @Override
    public UnidadeResponseDTO getById(Long id) {
        User currentUser = securityUtils.getCurrentUser();
        Unidade entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Unidade não encontrada"));
        
        if (!entity.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Você não tem permissão para acessar esta unidade");
        }
        
        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User currentUser = securityUtils.getCurrentUser();
        Unidade entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Unidade não encontrada"));
        
        if (!entity.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Você não tem permissão para excluir esta unidade");
        }
        
        // 1. Deletar contratos relacionados à unidade (e suas parcelas e PDFs)
        List<Contrato> contratos = contratoRepository.findAllByUnidade_Id(id);
        for (Contrato contrato : contratos) {
            // Deletar parcelas do contrato
            List<Parcela> parcelas = parcelaRepository.findAllByContrato_Id(contrato.getId());
            for (Parcela parcela : parcelas) {
                parcelaRepository.deleteById(parcela.getId());
            }
            
            // Deletar PDF do Cloudinary se existir
            if (contrato.getPdfContrato() != null && !contrato.getPdfContrato().isEmpty()) {
                try {
                    cloudinaryService.deleteFileByUrl(contrato.getPdfContrato());
                } catch (Exception e) {
                    System.err.println("Erro ao excluir PDF do Cloudinary: " + e.getMessage());
                }
            }
            contratoRepository.deleteById(contrato.getId());
        }
        
        // 2. Deletar moradores relacionados à unidade
        List<Morador> moradores = moradorRepository.findAllByUnidade_Id(id);
        for (Morador morador : moradores) {
            moradorRepository.deleteById(morador.getId());
        }
        
        // 3. Deletar imagens da unidade do Cloudinary (se necessário)
        if (entity.getImagens() != null && !entity.getImagens().isEmpty()) {
            for (String imagemUrl : entity.getImagens()) {
                try {
                    cloudinaryService.deleteFileByUrl(imagemUrl);
                } catch (Exception e) {
                    System.err.println("Erro ao excluir imagem do Cloudinary: " + e.getMessage());
                }
            }
        }
        
        // 4. Finalmente, deletar a unidade
        repository.deleteById(id);
    }
}


