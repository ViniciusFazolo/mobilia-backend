package com.example.mobilia.dto.contrato;

import java.time.LocalDate;

import com.example.mobilia.domain.Contrato.ObjetoLocacao;


public record ContratoRequestDTO(
    Long id,
    LocalDate dataInicio,
    LocalDate dataFim,
    LocalDate dataVencimento,
    Double valorDeposito,
    ObjetoLocacao objLocacao,
    Integer qtd,
    Long user,
    Long morador) {

}

