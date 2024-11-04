package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Transacao;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    List<Transacao> findByOrigemIdOrDestinoId(Long origemId, Long destinoId);
}
