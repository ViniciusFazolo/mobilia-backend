package com.example.mobilia.dto.imovel;

import org.springframework.web.multipart.MultipartFile;

public record ImovelRequestDTO(
    Long id,
    String nome,
    String cep,
    String estado,
    String cidade,
    String bairro,
    String rua,
    Integer numero,
    String complemento,
    Boolean ativo,
    MultipartFile imagens) {

}
