package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Instituicao;

public interface InstituicaoRepository extends JpaRepository<Instituicao, Integer> {
}
