package com.example.mobilia.services;

import org.springframework.stereotype.Service;

import com.example.mobilia.dto.auth.LoginRequestDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

   public boolean empresaAutenticacao(LoginRequestDTO request) {
      return true;
   }

   public boolean alunoAutenticacao(LoginRequestDTO request) {
      return true;
   }

   public boolean servidorAutenticacao(LoginRequestDTO request) {
      return true;
   }
}
