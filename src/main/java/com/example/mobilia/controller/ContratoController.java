package com.example.mobilia.controller;


import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mobilia.dto.contrato.ContratoRequestDTO;
import com.example.mobilia.dto.contrato.ContratoResponseDTO;
import com.example.mobilia.services.ContratoService;

@RestController
@RequestMapping("/api/contrato")
public class ContratoController extends GenericController<ContratoRequestDTO, ContratoResponseDTO, Long> {
    
    private final ContratoService service;

    public ContratoController(ContratoService service) {
        super(service);
        this.service = service;
    }
    
    @GetMapping("/{id}/download")
    public ResponseEntity<ByteArrayResource> downloadPdf(@PathVariable Long id) {
        byte[] pdfBytes = service.downloadPdf(id);
        
        ByteArrayResource resource = new ByteArrayResource(pdfBytes);
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=contrato_" + id + ".pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .contentLength(pdfBytes.length)
            .body(resource);
    }

    @GetMapping("/{id}/view")
    public ResponseEntity<ByteArrayResource> visualizarPdf(@PathVariable Long id) {
        try {
            byte[] pdfBytes = service.downloadPdf(id);
            
            if (pdfBytes == null || pdfBytes.length == 0) {
                return ResponseEntity.notFound().build();
            }
            
            ByteArrayResource resource = new ByteArrayResource(pdfBytes);
            
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=contrato_" + id + ".pdf")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .contentLength(pdfBytes.length)
                .body(resource);
                
        } catch (Exception e) {
            throw new RuntimeException("Erro ao visualizar PDF: " + e.getMessage(), e);
        }
    }

    @GetMapping("/byMoradorId/{id}")
    public ResponseEntity<List<ContratoResponseDTO>> getByMoradorId(@PathVariable Long id){
        return ResponseEntity.ok().body(service.getByMoradorId(id));
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
