package com.example.mobilia.dto.user;

import com.example.mobilia.domain.UserRole;

public record UserResponseDTO(
        Long id,
        String login,
        String email,
        String nome,
        Boolean ativo,
        String cpf,
        String rg,
        String endereco,
        String numero,
        String bairro,
        String cidade,
        String estado,
        String cep,
        String telefone,
        UserRole userRole) {
}
