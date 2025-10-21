package com.example.mobilia.dto.auth;

public record LoginResponseDTO(
                Long id,
                String usuario,
                Integer UserRole,
                String token) {

}
