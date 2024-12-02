package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.controller;

import java.util.List;

import org.springframework.http.MediaType;
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

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.instituicao.InstituicaoCreate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.instituicao.InstituicaoDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.instituicao.InstituicaoUpdate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.professor.ProfessorDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.service.InstituicaoService;

@RestController
@RequestMapping("/instituicoes")
public class InstituicaoController {

    private final InstituicaoService instituicaoService;

    public InstituicaoController(InstituicaoService instituicaoService) {
        this.instituicaoService = instituicaoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstituicaoDTO> getById(@PathVariable Long id) {
        InstituicaoDTO instituicaoDTO = instituicaoService.getById(id);
        return ResponseEntity.ok(instituicaoDTO);
    }

    @GetMapping
    public ResponseEntity<List<InstituicaoDTO>> getAll() {
        List<InstituicaoDTO> instituicoes = instituicaoService.getAll();
        return ResponseEntity.ok(instituicoes);
    }

    @PostMapping
    public ResponseEntity<InstituicaoDTO> create(@RequestBody @Validated InstituicaoCreate createDTO) {
        InstituicaoDTO instituicaoDTO = instituicaoService.create(createDTO);
        return ResponseEntity.status(201).body(instituicaoDTO);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InstituicaoDTO> update(@PathVariable Long id,
                                                 @RequestBody @Validated InstituicaoUpdate updateDTO) {
        InstituicaoDTO instituicaoDTO = instituicaoService.update(id, updateDTO);
        return ResponseEntity.ok(instituicaoDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        instituicaoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/novoSemestre")
    public ResponseEntity<Void> novoSemestre(@PathVariable Long id) {
        instituicaoService.novoSemestre(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/professores")
    public ResponseEntity<List<ProfessorDTO>> getProfessores(@PathVariable Long id) {
        List<ProfessorDTO> professores = instituicaoService.getProfessores(id);
        return ResponseEntity.ok(professores);
    }
    
}
