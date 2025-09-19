package com.example.mobilia.services;

import org.springframework.stereotype.Service;

import com.example.mobilia.domain.User;
import com.example.mobilia.dto.user.UserRequestDTO;
import com.example.mobilia.dto.user.UserResponseDTO;
import com.example.mobilia.mapper.UserMapper;
import com.example.mobilia.repository.UserRepository;

@Service
public class UserService extends CrudServiceImpl<User, UserRequestDTO, UserResponseDTO, Long> {

    public UserService(UserRepository repository, UserMapper mapper) {
        super(repository, mapper);
    }

    public String teste() {
        return "Hello world";
    }
}
