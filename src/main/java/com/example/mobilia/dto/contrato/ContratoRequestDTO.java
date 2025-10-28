package com.example.mobilia.dto.contrato;

import java.time.LocalDate;

import com.example.mobilia.domain.Contrato.ObjetoLocacao;


public record ContratoRequestDTO(
    Long id,
    LocalDate dataInicio,
    LocalDate dataFim,
    LocalDate dataVencimento,
    Double valorAluguel,
    Double valorDeposito,
    ObjetoLocacao objLocacao,
    Integer qtd,
    String cidade,
    String estado,
    String cep,
    String bairro,
    String rua,
    Integer numero,
    Long user,
    Long morador,
    Long unidade,
    Long imovel) {

}

