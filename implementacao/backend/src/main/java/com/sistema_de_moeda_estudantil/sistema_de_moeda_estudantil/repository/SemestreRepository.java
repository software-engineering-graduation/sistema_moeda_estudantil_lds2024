package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Semestre;

@Repository
public interface SemestreRepository extends JpaRepository<Semestre, Long> {
    
}
