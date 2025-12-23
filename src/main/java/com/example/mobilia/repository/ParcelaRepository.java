package com.example.mobilia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mobilia.domain.Parcela;
import com.example.mobilia.domain.User;

@Repository
public interface ParcelaRepository extends JpaRepository<Parcela, Long> {
    List<Parcela> findAllByContrato_Locador(User user);
    List<Parcela> findAllByContrato_Locador_Id(Long userId);
    List<Parcela> findAllByContrato_Id(Long contratoId);
}
