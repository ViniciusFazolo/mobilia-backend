package com.example.mobilia.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.mobilia.domain.User;
import com.example.mobilia.dto.user.UserRequestDTO;
import com.example.mobilia.dto.user.UserResponseDTO;

@Mapper(componentModel = "spring")
public interface UserMapper extends IGenericMapper<UserRequestDTO, UserResponseDTO, User> {

    @Override
    @Mapping(source = "userRole", target = "userRole.id")
    User toEntity(UserRequestDTO obj);
}
