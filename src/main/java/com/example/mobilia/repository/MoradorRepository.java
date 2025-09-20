package com.example.mobilia.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.mobilia.domain.Morador;

@Repository
public interface MoradorRepository extends CrudRepository<Morador, Long> {

}
