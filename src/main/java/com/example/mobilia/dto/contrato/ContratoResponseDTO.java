package com.example.mobilia.dto.contrato;

import java.time.LocalDate;

import com.example.mobilia.domain.Contrato.ObjetoLocacao;
import com.example.mobilia.domain.Imovel;
import com.example.mobilia.domain.Morador;
import com.example.mobilia.domain.Unidade;
import com.example.mobilia.domain.User;


public record ContratoResponseDTO(
    Long id,
    LocalDate dataInicio,
    LocalDate dataFim,
    LocalDate dataVencimento,
    Double valorDeposito,
    ObjetoLocacao objLocacao,
    Integer qtd,
    User userId,
    Morador morador,
    Unidade unidade,
    Imovel imovel) {
}
