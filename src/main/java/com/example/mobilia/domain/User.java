package com.example.mobilia.domain;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String login;

    @Column(unique = true)
    private String email;

    private String nome;
    private String cpf;
    private String rg;
    private String endereco;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String telefone;
    
    private String pw;
    private Boolean ativo = true;

    @Column(nullable = true, updatable = false)
    private LocalDateTime dtCadastro;

    @PrePersist
    public void prePersist() {
        this.dtCadastro = LocalDateTime.now();
    }

    @ManyToOne
    private UserRole userRole;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (userRole == null || userRole.getId() == null) {
            return List.of(new SimpleGrantedAuthority("ROLE_USUARIO")); 
        }
        
        if (userRole.getId() == 1)
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        else if (userRole.getId() == 2)
            return List.of(new SimpleGrantedAuthority("ROLE_USUARIO"));
        else
            return List.of(new SimpleGrantedAuthority("ROLE_USUARIO"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return null;
    }
}