package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Professor;

import jakarta.transaction.Transactional;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    @Query("UPDATE Usuario u SET u.saldoMoedas = u.saldoMoedas + :increaseValue WHERE u.tipo = 'PROFESSOR'")
    @Modifying
    @Transactional
    void reabastecerSaldo(Double increaseValue);

    Optional<Professor> findByEmail(String email);
}
