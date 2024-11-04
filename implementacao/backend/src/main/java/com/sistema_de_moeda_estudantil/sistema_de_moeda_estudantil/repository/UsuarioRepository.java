package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
