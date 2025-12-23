package com.example.mobilia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mobilia.domain.Morador;
import com.example.mobilia.domain.User;

@Repository
public interface MoradorRepository extends JpaRepository<Morador, Long> {
    List<Morador> findAllByUser(User user);
    List<Morador> findAllByUser_Id(Long userId);
    List<Morador> findAllByUnidade_User_Id(Long userId);
    List<Morador> findAllByImovel_Id(Long imovelId);
    List<Morador> findAllByUnidade_Id(Long unidadeId);
}
