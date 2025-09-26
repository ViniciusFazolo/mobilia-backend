package com.example.mobilia.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Morador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Pessoal
    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    private String rg;
    private Boolean ativo = true;

    // Contrato
    private LocalDate dtVencimento;
    private LocalDate dtInicio;
    private LocalDate dtFim;

    @ManyToOne
    @JoinColumn(name = "unidade_id", nullable = false)
    private Unidade unidade;

    @ManyToOne
    @JoinColumn(name = "imovel_id", nullable = false)
    private Imovel imovel;

    private LocalDateTime dtCadastro;

    @PrePersist
    public void prePersist() {
        this.dtCadastro = LocalDateTime.now();
    }
}
