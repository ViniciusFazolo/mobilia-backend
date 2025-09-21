package com.example.mobilia.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Imovel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Identificacao
    private String nome;
    private String cep;
    private String estado;
    private String cidade;
    private String bairro;
    private String rua;
    private Integer numero;
    private String complemento;
    private String imagem;
    private Boolean ativo;

    @Column(nullable = true, updatable = false)
    private LocalDateTime dtCadastro;

    @PrePersist
    public void prePersist() {
        this.dtCadastro = LocalDateTime.now();
    }
}
