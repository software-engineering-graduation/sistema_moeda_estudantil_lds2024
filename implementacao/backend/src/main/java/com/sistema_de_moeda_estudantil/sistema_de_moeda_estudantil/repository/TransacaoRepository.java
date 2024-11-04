package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
}
