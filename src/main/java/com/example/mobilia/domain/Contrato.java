package com.example.mobilia.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
public class Contrato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Dados do contrato
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private LocalDate dataVencimento;
    private Double valorAluguel;
    private Double valorDeposito;
    private Boolean status;

    // Objeto da locação
    @Enumerated(EnumType.STRING)
    private ObjetoLocacao objLocacao;
    private Integer qtd;
    private String cidade;
    private String estado;
    private String cep;
    private String bairro;
    private String rua;
    private Integer numero;
    
    // Dados do locatário
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User locador;

    // Dados do locador
    @ManyToOne
    @JoinColumn(name = "morador_id", nullable = false)
    private Morador morador;
    
    // Dados da unidade
    @ManyToOne
    @JoinColumn(name = "unidade_id", nullable = false)
    private Unidade unidade;
    
    // Dados do imóvel
    @ManyToOne
    @JoinColumn(name = "imovel_id", nullable = false)
    private Imovel imovel;
    
    // Controle
    private LocalDateTime dtCadastro;
    private String pdfContrato;
    
    @PrePersist
    public void prePersist() {
        this.dtCadastro = LocalDateTime.now();
    }

    public enum ObjetoLocacao {
        CASA_RESIDENCIAL,
        APARTAMENTO_RESIDENCIAL,
        PONTO_COMERCIAL,
    }
}
