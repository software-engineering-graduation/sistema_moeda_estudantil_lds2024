package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Vantagem;

public interface VantagemRepository extends JpaRepository<Vantagem, Long> {
    /**
     * Find all vantagens associated with a specific empresa.
     *
     * @param empresaId the ID of the empresa
     * @return a list of vantagens
     */
    List<Vantagem> findByEmpresaId(Long empresaId);

    /**
     * Find a specific vantagem by its ID and associated empresa ID.
     *
     * @param id        the ID of the vantagem
     * @param empresaId the ID of the empresa
     * @return an Optional containing the vantagem if found, or empty if not found
     */
    Optional<Vantagem> findByIdAndEmpresaId(Long id, Long empresaId);

    /**
     * Delete all vantagens associated with a specific empresa.
     *
     * @param empresaId the ID of the empresa
     */
    void deleteByEmpresaId(Long empresaId);

    /**
     * Check if a vantagem exists for a given empresa.
     *
     * @param id        the ID of the vantagem
     * @param empresaId the ID of the empresa
     * @return true if the vantagem exists, false otherwise
     */
    boolean existsByIdAndEmpresaId(Long id, Long empresaId);
}
