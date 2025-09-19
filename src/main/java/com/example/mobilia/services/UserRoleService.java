package com.example.mobilia.services;

import org.springframework.stereotype.Service;

import com.example.mobilia.domain.UserRole;
import com.example.mobilia.dto.userrole.UserRoleDTO;
import com.example.mobilia.mapper.UserRoleMapper;
import com.example.mobilia.repository.UserRoleRepository;

@Service
public class UserRoleService extends CrudServiceImpl<UserRole, UserRoleDTO, UserRoleDTO, Long> {

    public UserRoleService(UserRoleRepository repository, UserRoleMapper mapper) {
        super(repository, mapper);
    }

}
