package com.example.mobilia.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Unidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Identificacao
    private String identificacao;
    private String bloco;
    private String complemento;

    //Valor
    private Double valorAluguel;

    //Caracteristicas
    private Double areaTotal;
    private Integer qtdBanheiro;
    private Integer qtdQuarto;
    private Integer qtdSuite;
    private Integer qtdGaragem;
    private Integer qtdSala;
    private Boolean cozinha;
    private Boolean areaServico;
    private String descricao;

    //Controle
    @Enumerated(EnumType.STRING)
    private StatusUnidade status; 

    private List<String> imagens = new ArrayList<>();
    private Boolean ativo;

    @ManyToOne
    @JoinColumn(name = "imovel_id", nullable = false)
    private Imovel imovel;

    @Column(nullable = true, updatable = false)
    private LocalDateTime dtCadastro;

    @PrePersist
    public void prePersist() {
        this.dtCadastro = LocalDateTime.now();
    }

    public enum StatusUnidade {
        OCUPADA,
        VAZIA
    }
}
