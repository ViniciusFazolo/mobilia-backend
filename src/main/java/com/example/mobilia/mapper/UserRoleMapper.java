package com.example.mobilia.mapper;

import org.mapstruct.Mapper;

import com.example.mobilia.domain.UserRole;
import com.example.mobilia.dto.userrole.UserRoleDTO;

@Mapper(componentModel = "spring")
public interface UserRoleMapper extends IGenericMapper<UserRoleDTO, UserRoleDTO, UserRole> {

}
