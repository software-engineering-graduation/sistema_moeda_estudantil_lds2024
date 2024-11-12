package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.FuncionarioEmpresa;

@Repository
public interface FuncionarioEmpresaRepository extends JpaRepository<FuncionarioEmpresa, Long> {
        
        List<FuncionarioEmpresa> findByEmpresaId(Long empresaId);

        Optional<FuncionarioEmpresa> findByIdAndEmpresaId(Long funcionarioId, Long empresaId);

        Optional<FuncionarioEmpresa> findByEmail(String name);
}