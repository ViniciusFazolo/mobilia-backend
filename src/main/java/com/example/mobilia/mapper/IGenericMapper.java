package com.example.mobilia.mapper;

public interface IGenericMapper<REQ, RES, E> {
    E toEntity(REQ dto);

    RES toDto(E entity);
}