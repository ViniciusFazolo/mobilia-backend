package com.example.mobilia.dto.imovel;

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
    String imgUrl) {

}
