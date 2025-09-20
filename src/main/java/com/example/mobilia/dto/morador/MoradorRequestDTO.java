package com.example.mobilia.dto.morador;

import java.time.LocalDate;

public record MoradorRequestDTO(
    Long id,
    String nome,
    String email,
    String telefone,
    String cpf,
    String rg,
    LocalDate dtNascimento,
    Boolean ativo,
    LocalDate dtVencimento,
    LocalDate dtInicio,
    LocalDate dtFim,
    Long unidade,
    Long imovel) {

}
