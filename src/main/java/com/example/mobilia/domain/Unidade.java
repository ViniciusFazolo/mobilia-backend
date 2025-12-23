package com.example.mobilia.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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

    @ElementCollection
    @CollectionTable(name = "unidade_imagens", joinColumns = @JoinColumn(name = "unidade_id"))
    @Column(name = "imagem_url")
    private List<String> imagens = new ArrayList<>();
    private Boolean ativo = true;

    @ManyToOne
    @JoinColumn(name = "imovel_id", nullable = false)
    private Imovel imovel;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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
