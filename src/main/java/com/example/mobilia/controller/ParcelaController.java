package com.example.mobilia.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mobilia.dto.parcela.ParcelaRequestDTO;
import com.example.mobilia.dto.parcela.ParcelaResponseDTO;
import com.example.mobilia.services.ParcelaService;

@RestController
@RequestMapping("/api/parcela")
public class ParcelaController extends GenericController<ParcelaRequestDTO, ParcelaResponseDTO, Long> {

    private final ParcelaService service;

    protected ParcelaController(ParcelaService service) {
        super(service);
        this.service = service;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
