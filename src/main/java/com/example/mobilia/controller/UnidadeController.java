package com.example.mobilia.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mobilia.dto.unidade.UnidadeRequestDTO;
import com.example.mobilia.dto.unidade.UnidadeResponseDTO;
import com.example.mobilia.services.UnidadeService;

@RestController
@RequestMapping("/api/unidade")
public class UnidadeController extends GenericController<UnidadeRequestDTO, UnidadeResponseDTO, Long> {

    private final UnidadeService service;

    public UnidadeController(UnidadeService service) {
        super(service);
        this.service = service;
    }

    @Override
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<UnidadeResponseDTO> create(@ModelAttribute UnidadeRequestDTO entity) {
        return ResponseEntity.ok().body(service.create(entity));
    }

    @Override
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<UnidadeResponseDTO> update(@PathVariable Long id, @ModelAttribute UnidadeRequestDTO entity) {
        return ResponseEntity.ok().body(service.update(id, entity));
    }

}
