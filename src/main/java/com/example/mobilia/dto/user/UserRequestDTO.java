package com.example.mobilia.dto.user;

public record UserRequestDTO(
                Long id,
                String login,
                String email,
                String nome,
                String pw,
                Boolean ativo,
                Integer userRole) {

}
