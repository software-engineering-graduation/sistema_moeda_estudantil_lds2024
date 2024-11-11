package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.CupomResgate;

@Repository
public interface CupomResgateRepository extends JpaRepository<CupomResgate, Long> {
    List<CupomResgate> findByAlunoId(Long alunoId);
}