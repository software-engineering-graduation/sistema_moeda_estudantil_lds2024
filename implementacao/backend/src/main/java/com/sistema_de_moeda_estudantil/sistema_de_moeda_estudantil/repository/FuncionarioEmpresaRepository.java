package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository;

import java.util.List;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.FuncionarioEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncionarioEmpresaRepository extends JpaRepository<FuncionarioEmpresa, Integer> {
        
        List<FuncionarioEmpresa> findByEmpresaId(int empresaId);
}