package com.example.mobilia.dto.user;

public record UserRequestDTO(
                Long id,
                String login,
                String email,
                String nome,
                String pw,
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
                Integer userRole) {

}
