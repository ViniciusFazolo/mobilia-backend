package com.example.mobilia.dto.unidade;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.mobilia.domain.Unidade.StatusUnidade;

public record UnidadeRequestDTO(
    Long id,
    String identificacao,
    String bloco,
    String complemento,
    Double valorAluguel,
    Double areaTotal,
    Integer qtdBanheiro,
    Integer qtdQuarto,
    Integer qtdSuite,
    Integer qtdGaragem,
    Integer qtdSala,
    Boolean cozinha,
    Boolean areaServico,
    StatusUnidade status,
    String descricao,
    List<MultipartFile> imagens,
    Boolean ativo,
    Long imovel) {

}
