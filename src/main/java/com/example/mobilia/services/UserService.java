package com.example.mobilia.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.mobilia.domain.User;
import com.example.mobilia.domain.UserRole;
import com.example.mobilia.dto.user.UserRequestDTO;
import com.example.mobilia.dto.user.UserResponseDTO;
import com.example.mobilia.mapper.UserMapper;
import com.example.mobilia.repository.UserRepository;
import com.example.mobilia.utils.SecurityUtils;

import jakarta.transaction.Transactional;

@Service
public class UserService extends CrudServiceImpl<User, UserRequestDTO, UserResponseDTO, Long> {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtils securityUtils;

    public UserService(UserRepository repository, UserMapper mapper, PasswordEncoder passwordEncoder, SecurityUtils securityUtils) {
        super(repository, mapper);
        this.userRepository = repository;
        this.userMapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.securityUtils = securityUtils;
    }

    @Override
    @Transactional
    public UserResponseDTO update(Long id, UserRequestDTO dto) {
        User currentUser = securityUtils.getCurrentUser();
        User userToUpdate = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        // Verifica permissões: usuário só pode editar seu próprio perfil, exceto admin
        boolean isAdmin = currentUser.getUserRole() != null && currentUser.getUserRole().getId() != null && currentUser.getUserRole().getId() == 1;
        boolean isEditingOwnProfile = currentUser.getId().equals(id);
        
        if (!isAdmin && !isEditingOwnProfile) {
            throw new RuntimeException("Você não tem permissão para editar este usuário");
        }
        
        // Atualizar dados básicos
        userToUpdate.setNome(dto.nome());
        userToUpdate.setEmail(dto.email());
        userToUpdate.setCpf(dto.cpf());
        userToUpdate.setRg(dto.rg());
        userToUpdate.setEndereco(dto.endereco());
        userToUpdate.setNumero(dto.numero());
        userToUpdate.setBairro(dto.bairro());
        userToUpdate.setCidade(dto.cidade());
        userToUpdate.setEstado(dto.estado());
        userToUpdate.setCep(dto.cep());
        userToUpdate.setTelefone(dto.telefone());
        userToUpdate.setAtivo(dto.ativo());
        
        // Atualizar login se fornecido e não duplicado
        if (dto.login() != null && !dto.login().isEmpty()) {
            // Verifica se o login já existe em outro usuário
            userRepository.findByLogin(dto.login())
                .ifPresent(existingUser -> {
                    if (!existingUser.getId().equals(id)) {
                        throw new RuntimeException("Este login já está em uso");
                    }
                });
            userToUpdate.setLogin(dto.login());
        }
        
        // Atualizar email se fornecido e não duplicado
        if (dto.email() != null && !dto.email().isEmpty()) {
            // Verifica se o email já existe em outro usuário
            userRepository.findByEmail(dto.email())
                .ifPresent(existingUser -> {
                    if (!existingUser.getId().equals(id)) {
                        throw new RuntimeException("Este e-mail já está em uso");
                    }
                });
            userToUpdate.setEmail(dto.email());
        }
        
        // Atualizar senha apenas se fornecida (não nula e não vazia)
        if (dto.pw() != null && !dto.pw().isEmpty()) {
            userToUpdate.setPw(passwordEncoder.encode(dto.pw()));
        }
        
        // Atualizar role apenas se for admin
        if (isAdmin && dto.userRole() != null) {
            userToUpdate.setUserRole(new UserRole(dto.userRole()));
        }
        
        userToUpdate = userRepository.save(userToUpdate);
        return userMapper.toDto(userToUpdate);
    }

    public String teste() {
        return "Hello world";
    }
}
