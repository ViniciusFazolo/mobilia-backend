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
    User userId,
    Morador morador,
    Unidade unidade,
    Imovel imovel) {
}
