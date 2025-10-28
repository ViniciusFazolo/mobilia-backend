package com.example.mobilia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mobilia.domain.Parcela;

@Repository
public interface ParcelaRepository extends JpaRepository<Parcela, Long> {

}
