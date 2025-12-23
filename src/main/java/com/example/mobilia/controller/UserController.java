package com.example.mobilia.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mobilia.dto.user.UserRequestDTO;
import com.example.mobilia.dto.user.UserResponseDTO;
import com.example.mobilia.services.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController extends GenericController<UserRequestDTO, UserResponseDTO, Long> {

    private final UserService service;

    protected UserController(UserService service) {
        super(service);
        this.service = service;
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @RequestBody UserRequestDTO entity) {
        return ResponseEntity.ok().body(service.update(id, entity));
    }

}
