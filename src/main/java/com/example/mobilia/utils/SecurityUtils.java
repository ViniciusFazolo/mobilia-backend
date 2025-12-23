package com.example.mobilia.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.mobilia.domain.User;
import com.example.mobilia.exceptions.PadraoException;
import com.example.mobilia.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SecurityUtils {
    
    private final UserRepository userRepository;
    
    /**
     * Obtém o usuário autenticado do SecurityContext
     * @return User autenticado
     * @throws PadraoException se não houver usuário autenticado
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null) {
            throw new PadraoException("Token de autenticação não fornecido. Por favor, faça login e envie o token no header Authorization: Bearer <token>");
        }
        
        Object principal = authentication.getPrincipal();
        
        // Verifica se é uma autenticação anônima do Spring Security
        if (principal instanceof String && "anonymousUser".equals(principal)) {
            throw new PadraoException("Token de autenticação não fornecido ou inválido. Por favor, faça login e envie o token no header Authorization: Bearer <token>");
        }
        
        if (!authentication.isAuthenticated() || "anonymousUser".equals(principal)) {
            throw new PadraoException("Token inválido ou expirado. Por favor, faça login novamente.");
        }
        
        // Se o principal é uma instância de User, busca novamente do banco para garantir que está anexado ao Hibernate
        if (principal instanceof User) {
            User user = (User) principal;
            try {
                // Busca novamente do banco para garantir que está anexado à sessão do Hibernate
                return userRepository.findById(user.getId())
                    .orElseThrow(() -> new PadraoException("Usuário não encontrado no banco de dados. ID: " + user.getId()));
            } catch (Exception e) {
                // Se falhar ao buscar pelo ID, tenta pelo login
                if (user.getLogin() != null) {
                    return userRepository.findByLogin(user.getLogin())
                        .orElseThrow(() -> new PadraoException("Usuário não encontrado no banco de dados. Login: " + user.getLogin()));
                }
                throw new PadraoException("Erro ao buscar usuário: " + e.getMessage());
            }
        }
        
        // Se o principal é uma String (login), busca o usuário no banco
        if (principal instanceof String) {
            String login = (String) principal;
            return userRepository.findByLogin(login)
                .orElseThrow(() -> new PadraoException("Usuário não encontrado no banco de dados. Login: " + login + ". Verifique se o login está correto e se o usuário existe."));
        }
        
        // Se o principal é null ou de outro tipo
        throw new PadraoException("Usuário não encontrado no contexto de segurança. Tipo do principal: " + 
            (principal != null ? principal.getClass().getName() : "null") + ". Por favor, faça login novamente.");
    }
    
    /**
     * Obtém o ID do usuário autenticado
     * @return ID do usuário
     */
    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }
}
