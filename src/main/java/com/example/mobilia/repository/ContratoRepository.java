package com.example.mobilia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mobilia.domain.Contrato;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {
    List<Contrato> findAllByMorador_Id(Long id);
    List<Contrato> findAllByLocador_Id(Long userId);
    List<Contrato> findAllByImovel_Id(Long imovelId);
    List<Contrato> findAllByUnidade_Id(Long unidadeId);
}
