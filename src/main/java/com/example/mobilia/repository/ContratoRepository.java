package com.example.mobilia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mobilia.domain.Contrato;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {
    
}
