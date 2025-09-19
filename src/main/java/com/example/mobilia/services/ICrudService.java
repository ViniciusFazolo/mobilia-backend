package com.example.mobilia.services;

public interface ICrudService<REQ, RES, ID> {

    Iterable<RES> getAll();

    RES getById(ID id);

    boolean existsById(ID id);

    RES create(REQ entity);

    RES update(ID id, REQ entity);

    void delete(ID id);
}