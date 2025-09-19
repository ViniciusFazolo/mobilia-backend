package com.example.mobilia.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mobilia.dto.user.UserRequestDTO;
import com.example.mobilia.dto.user.UserResponseDTO;
import com.example.mobilia.services.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController extends GenericController<UserRequestDTO, UserResponseDTO, Long> {

    protected UserController(UserService service) {
        super(service);
    }

}
