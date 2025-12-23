package com.example.mobilia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mobilia.domain.Unidade;
import com.example.mobilia.domain.User;

@Repository
public interface UnidadeRepository extends JpaRepository<Unidade, Long> {
    List<Unidade> findAllByUser(User user);
    List<Unidade> findAllByUser_Id(Long userId);
    List<Unidade> findAllByImovel_User_Id(Long userId);
    List<Unidade> findAllByImovel_Id(Long imovelId);
}
