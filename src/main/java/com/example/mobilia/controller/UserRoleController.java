package com.example.mobilia.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mobilia.dto.userrole.UserRoleDTO;
import com.example.mobilia.services.UserRoleService;

@RestController
@RequestMapping("/api/userrole")
public class UserRoleController extends GenericController<UserRoleDTO, UserRoleDTO, Long> {

    protected UserRoleController(UserRoleService service) {
        super(service);
    }

}
