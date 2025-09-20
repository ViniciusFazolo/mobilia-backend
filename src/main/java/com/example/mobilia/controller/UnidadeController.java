package com.example.mobilia.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mobilia.dto.unidade.UnidadeRequestDTO;
import com.example.mobilia.dto.unidade.UnidadeResponseDTO;
import com.example.mobilia.services.UnidadeService;

@RestController
@RequestMapping("/api/unidade")
public class UnidadeController extends GenericController<UnidadeRequestDTO, UnidadeResponseDTO, Long> {

    public UnidadeController(UnidadeService service) {
        super(service);
    }

}
