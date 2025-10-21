package com.example.mobilia.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mobilia.dto.parcela.ParcelaRequestDTO;
import com.example.mobilia.dto.parcela.ParcelaResponseDTO;
import com.example.mobilia.services.ParcelaService;

@RestController
@RequestMapping("/api/parcela")
public class ParcelaController extends GenericController<ParcelaRequestDTO, ParcelaResponseDTO, Long> {

    protected ParcelaController(ParcelaService service) {
        super(service);
    }

}
