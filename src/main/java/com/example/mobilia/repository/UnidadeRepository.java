package com.example.mobilia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mobilia.domain.Unidade;

@Repository
public interface UnidadeRepository extends JpaRepository<Unidade, Long> {

}
