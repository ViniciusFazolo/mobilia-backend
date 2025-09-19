package com.example.mobilia.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.mobilia.services.ICrudService;

public abstract class GenericController<REQ, RES, ID> {

    private final ICrudService<REQ, RES, ID> service;

    protected GenericController(ICrudService<REQ, RES, ID> service) {
        this.service = service;
    }

    @GetMapping
    public Iterable<RES> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RES> getById(@PathVariable ID id) {
        RES entity = service.getById(id);
        return ResponseEntity.ok().body(entity);
    }

    @PostMapping
    public ResponseEntity<RES> create(@RequestBody REQ entity) {
        return ResponseEntity.ok().body(service.create(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RES> update(@PathVariable ID id, @RequestBody REQ entity) {
        return ResponseEntity.ok().body(service.update(id, entity));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RES> partialUpdate(@PathVariable ID id, @RequestBody REQ updates) {
        return ResponseEntity.ok().body(service.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        if (!service.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}