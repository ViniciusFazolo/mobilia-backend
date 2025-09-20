package com.example.mobilia.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mobilia.dto.morador.MoradorRequestDTO;
import com.example.mobilia.dto.morador.MoradorResponseDTO;
import com.example.mobilia.services.MoradorService;

@RestController
@RequestMapping("/api/morador")
public class MoradorController extends GenericController<MoradorRequestDTO, MoradorResponseDTO, Long> {
    
    protected MoradorController(MoradorService service) {
        super(service);
    }

}
