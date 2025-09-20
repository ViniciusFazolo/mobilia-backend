package com.example.mobilia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mobilia.domain.Imovel;

@Repository
public interface ImovelRepository extends JpaRepository<Imovel, Long> {

}
