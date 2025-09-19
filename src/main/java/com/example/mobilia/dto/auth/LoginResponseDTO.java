package com.example.mobilia.dto.auth;

public record LoginResponseDTO(
                String usuario,
                Integer UserRole,
                String token) {

}
