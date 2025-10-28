package com.example.mobilia.dto.parcela;

import java.time.LocalDate;

import com.example.mobilia.domain.Contrato;
import com.example.mobilia.domain.Parcela.StatusParcela;

public record ParcelaResponseDTO(
    Long id,
    Contrato contrato,
    Integer numeroParcela,
    LocalDate dataVencimento,
    Double valor,
    LocalDate dataPagamento,
    StatusParcela status
) {

}
