package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Usuario;

public interface UserRepository extends CrudRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
  }
  