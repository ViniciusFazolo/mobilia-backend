package com.example.mobilia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mobilia.domain.Morador;

@Repository
public interface MoradorRepository extends JpaRepository<Morador, Long> {

}
