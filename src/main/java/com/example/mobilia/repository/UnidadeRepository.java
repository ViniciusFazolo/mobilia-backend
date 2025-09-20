package com.example.mobilia.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.mobilia.domain.Unidade;

@Repository
public interface UnidadeRepository extends CrudRepository<Unidade, Long> {

}
