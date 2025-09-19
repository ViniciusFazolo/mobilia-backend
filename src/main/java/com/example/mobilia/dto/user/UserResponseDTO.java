package com.example.mobilia.dto.user;

import com.example.mobilia.domain.UserRole;

public record UserResponseDTO(
        Long id,
        String login,
        String email,
        String nome,
        Boolean ativo,
        UserRole userRole) {
}
