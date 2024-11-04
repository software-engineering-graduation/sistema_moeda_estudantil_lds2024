package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.professor.ProfessorCreate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.professor.ProfessorDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.transacao.TransacaoDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.service.ProfessorService;

@RestController
@RequestMapping("/professores")
public class ProfessorController {

    private final ProfessorService professorService;

    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorDTO> getById(@PathVariable Long id) {
        ProfessorDTO professorDTO = professorService.getById(id);
        return ResponseEntity.ok(professorDTO);
    }

    @GetMapping
    public ResponseEntity<List<ProfessorDTO>> getAll() {
        List<ProfessorDTO> professores = professorService.getAll();
        return ResponseEntity.ok(professores);
    }

    @PostMapping
    public ResponseEntity<ProfessorDTO> create(@RequestBody @Validated ProfessorCreate createDTO) {
        ProfessorDTO professorDTO = professorService.create(createDTO);
        return ResponseEntity.status(201).body(professorDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessorDTO> update(@PathVariable Long id,
            @RequestBody @Validated ProfessorDTO updateDTO) {
        ProfessorDTO professorDTO = professorService.update(id, updateDTO);
        return ResponseEntity.ok(professorDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        professorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/transacoes")
    public ResponseEntity<List<TransacaoDTO>> getTransacoes(@PathVariable Long id) {
        List<TransacaoDTO> transacoes = professorService.getTransacoes(id);
        return ResponseEntity.ok(transacoes);
    }
}
