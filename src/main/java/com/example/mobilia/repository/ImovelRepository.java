package com.example.mobilia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mobilia.domain.Imovel;
import com.example.mobilia.domain.User;

@Repository
public interface ImovelRepository extends JpaRepository<Imovel, Long> {
    List<Imovel> findAllByUser(User user);
    List<Imovel> findAllByUser_Id(Long userId);
}
