package com.example.mobilia.dto.unidade;

import com.example.mobilia.domain.Imovel;
import com.example.mobilia.domain.Unidade.StatusUnidade;

public record UnidadeResponseDTO(
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
    String imgUrl,
    Boolean ativo,
    Imovel imovel) {

}
