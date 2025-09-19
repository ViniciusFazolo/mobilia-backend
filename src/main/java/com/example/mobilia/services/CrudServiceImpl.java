package com.example.mobilia.services;

import java.util.stream.StreamSupport;

import org.springframework.data.repository.CrudRepository;

import com.example.mobilia.exceptions.PadraoException;
import com.example.mobilia.mapper.IGenericMapper;

import jakarta.transaction.Transactional;

public abstract class CrudServiceImpl<E, REQ, RES, ID> implements ICrudService<REQ, RES, ID> {

    private final CrudRepository<E, ID> repository;
    private final IGenericMapper<REQ, RES, E> mapper;

    public CrudServiceImpl(CrudRepository<E, ID> repository, IGenericMapper<REQ, RES, E> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Iterable<RES> getAll() {
        Iterable<E> entities = repository.findAll();

        return StreamSupport.stream(entities.spliterator(), false)
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    @Override
    public RES getById(ID id) {
        E entity = repository.findById(id).orElseThrow(() -> new PadraoException("Registro não encontrado"));

        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    public RES create(REQ entity) {
        return mapper.toDto(repository.save(mapper.toEntity(entity)));
    };

    @Override
    @Transactional
    public RES update(ID id, REQ entity) {
        if (!repository.existsById(id)) {
            throw new PadraoException("Registro não encontrado");
        }

        return mapper.toDto(repository.save(mapper.toEntity(entity)));
    }

    @Override
    @Transactional
    public void delete(ID id) {
        repository.deleteById(id);
    }
}