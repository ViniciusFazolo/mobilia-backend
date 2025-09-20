package com.example.mobilia.dto.morador;

import java.time.LocalDate;

import com.example.mobilia.domain.Imovel;
import com.example.mobilia.domain.Unidade;

public record MoradorResponseDTO(
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
    Unidade unidade,
    Imovel imovel) {

}
