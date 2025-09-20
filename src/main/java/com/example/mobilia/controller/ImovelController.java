package com.example.mobilia.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mobilia.dto.imovel.ImovelRequestDTO;
import com.example.mobilia.dto.imovel.ImovelResponseDTO;
import com.example.mobilia.services.ImovelService;

@RestController
@RequestMapping("/api/imovel")
public class ImovelController extends GenericController<ImovelRequestDTO, ImovelResponseDTO, Long> {

    protected ImovelController(ImovelService service) {
        super(service);
    }

}
