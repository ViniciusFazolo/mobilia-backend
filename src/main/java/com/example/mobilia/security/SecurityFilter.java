package com.example.mobilia.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.mobilia.domain.User;
import com.example.mobilia.exceptions.PadraoException;
import com.example.mobilia.repository.UserRepository;
import com.example.mobilia.services.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final UserRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var token = this.recoverToken(request);
        
        // Se não houver token, continua sem autenticação (para permitir endpoints públicos)
        if (token == null || token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }
        
        var login = tokenService.validateToken(token);

        if (login != null) {
            User user = repository.findByLogin(login)
                    .orElseThrow(() -> new PadraoException("Usuário não encontrado"));
            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            // Se o token for inválido, limpa o contexto para evitar autenticação anônima
            SecurityContextHolder.clearContext();
        }
        
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null)
            return null;
        return authHeader.replace("Bearer ", "");
    }
}