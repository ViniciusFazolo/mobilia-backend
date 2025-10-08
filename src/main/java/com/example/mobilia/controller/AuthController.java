package com.example.mobilia.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mobilia.domain.User;
import com.example.mobilia.domain.UserRole;
import com.example.mobilia.dto.auth.LoginRequestDTO;
import com.example.mobilia.dto.auth.LoginResponseDTO;
import com.example.mobilia.dto.user.UserRequestDTO;
import com.example.mobilia.dto.user.UserResponseDTO;
import com.example.mobilia.exceptions.PadraoException;
import com.example.mobilia.mapper.UserMapper;
import com.example.mobilia.repository.UserRepository;
import com.example.mobilia.services.TokenService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final UserMapper mapper;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        User usuario = repository.findByLogin(request.login())
                .orElseThrow(() -> new PadraoException("Usuário ou senha incorretos"));

        if (!passwordEncoder.matches(request.senha(), usuario.getPw()))
            new PadraoException("Usuário ou senha incorretos");

        String token = tokenService.generateToken(usuario);
        return ResponseEntity.ok()
                .body(new LoginResponseDTO(usuario.getId(), usuario.getLogin(), usuario.getUserRole().getId(), token));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserRequestDTO request) {
        if (repository.findByLogin(request.login()).isPresent())
            throw new PadraoException("Esse login já está cadastrado");
        if (repository.findByEmail(request.email()).isPresent())
            throw new PadraoException("Esse e-mail já está cadastrado");

        try {
            User obj = mapper.toEntity(request);
            obj.setPw(passwordEncoder.encode(request.pw()));
            obj.setAtivo(true);
            obj.setUserRole(new UserRole(1));
            repository.save(obj);
            return ResponseEntity.ok().body(mapper.toDto(obj));
        } catch (Exception e) {
            throw new PadraoException("Erro ao registrar usuário");
        }
    }
}