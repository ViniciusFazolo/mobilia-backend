package com.example.mobilia.services;

import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import java.util.List;

import com.example.mobilia.domain.Contrato;
import com.example.mobilia.domain.Imovel;
import com.example.mobilia.domain.Morador;
import com.example.mobilia.domain.Parcela;
import com.example.mobilia.domain.Unidade;
import com.example.mobilia.domain.User;
import com.example.mobilia.dto.imovel.ImovelRequestDTO;
import com.example.mobilia.dto.imovel.ImovelResponseDTO;
import com.example.mobilia.mapper.ImovelMapper;
import com.example.mobilia.repository.ContratoRepository;
import com.example.mobilia.repository.ImovelRepository;
import com.example.mobilia.repository.MoradorRepository;
import com.example.mobilia.repository.ParcelaRepository;
import com.example.mobilia.repository.UnidadeRepository;
import com.example.mobilia.services.CloudinaryService;
import com.example.mobilia.utils.SecurityUtils;

import jakarta.transaction.Transactional;

@Service
public class ImovelService extends CrudServiceImpl<Imovel, ImovelRequestDTO, ImovelResponseDTO, Long> {

    private final ImovelRepository repository;
    private final ImovelMapper mapper;
    private final CloudinaryService cloudinaryService;
    private final SecurityUtils securityUtils;
    private final ContratoRepository contratoRepository;
    private final UnidadeRepository unidadeRepository;
    private final MoradorRepository moradorRepository;
    private final ParcelaRepository parcelaRepository;

    public ImovelService(ImovelRepository repository, ImovelMapper mapper, CloudinaryService cloudinaryService, SecurityUtils securityUtils, ContratoRepository contratoRepository, UnidadeRepository unidadeRepository, MoradorRepository moradorRepository, ParcelaRepository parcelaRepository) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.cloudinaryService = cloudinaryService;
        this.securityUtils = securityUtils;
        this.contratoRepository = contratoRepository;
        this.unidadeRepository = unidadeRepository;
        this.moradorRepository = moradorRepository;
        this.parcelaRepository = parcelaRepository;
    }

    @Override
    @Transactional
    public ImovelResponseDTO create(ImovelRequestDTO request) {
        try {
            User currentUser = securityUtils.getCurrentUser();
            Imovel entity = mapper.toEntity(request);
            entity.setUser(currentUser);

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
        User currentUser = securityUtils.getCurrentUser();
        
        // Busca a entidade existente do banco e verifica se pertence ao usuário
        Imovel entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Imóvel não encontrado"));
        
        if (!entity.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Você não tem permissão para atualizar este imóvel");
        }

        try {

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

    @Override
    public Iterable<ImovelResponseDTO> getAll() {
        User currentUser = securityUtils.getCurrentUser();
        Iterable<Imovel> entities = repository.findAllByUser(currentUser);

        return StreamSupport.stream(entities.spliterator(), false)
                .map(mapper::toDto)
                .toList();
    }
    
    @Override
    public ImovelResponseDTO getById(Long id) {
        User currentUser = securityUtils.getCurrentUser();
        Imovel entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Imóvel não encontrado"));
        
        if (!entity.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Você não tem permissão para acessar este imóvel");
        }
        
        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User currentUser = securityUtils.getCurrentUser();
        Imovel entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Imóvel não encontrado"));
        
        if (!entity.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Você não tem permissão para excluir este imóvel");
        }
        
        // 1. Deletar contratos relacionados (e suas parcelas e PDFs)
        List<Contrato> contratos = contratoRepository.findAllByImovel_Id(id);
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
        
        // 2. Buscar todas as unidades do imóvel
        List<Unidade> unidades = unidadeRepository.findAllByImovel_Id(id);
        
        // 3. Para cada unidade, deletar os moradores relacionados
        for (Unidade unidade : unidades) {
            List<Morador> moradores = moradorRepository.findAllByUnidade_Id(unidade.getId());
            for (Morador morador : moradores) {
                moradorRepository.deleteById(morador.getId());
            }
            // Deletar a unidade
            unidadeRepository.deleteById(unidade.getId());
        }
        
        // 4. Deletar moradores que possam estar diretamente ligados ao imóvel (caso existam)
        List<Morador> moradoresDiretos = moradorRepository.findAllByImovel_Id(id);
        for (Morador morador : moradoresDiretos) {
            moradorRepository.deleteById(morador.getId());
        }
        
        // 5. Deletar imagem do Cloudinary se existir
        if (entity.getImagem() != null && !entity.getImagem().isEmpty()) {
            try {
                cloudinaryService.deleteFileByUrl(entity.getImagem());
            } catch (Exception e) {
                System.err.println("Erro ao excluir imagem do Cloudinary: " + e.getMessage());
            }
        }
        
        // 6. Finalmente, deletar o imóvel
        repository.deleteById(id);
    }
}

