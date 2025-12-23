package com.example.mobilia.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mobilia.dto.imovel.ImovelRequestDTO;
import com.example.mobilia.dto.imovel.ImovelResponseDTO;
import com.example.mobilia.services.ImovelService;

@RestController
@RequestMapping("/api/imovel")
public class ImovelController extends GenericController<ImovelRequestDTO, ImovelResponseDTO, Long> {

    private final ImovelService service;

    protected ImovelController(ImovelService service) {
        super(service);
        this.service = service;
    }
    
    @Override
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ImovelResponseDTO> create(@ModelAttribute ImovelRequestDTO entity) {
        return ResponseEntity.ok().body(service.create(entity));
    }

    @Override
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<ImovelResponseDTO> update(@PathVariable Long id, @ModelAttribute ImovelRequestDTO entity) {
        return ResponseEntity.ok().body(service.update(id, entity));
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
