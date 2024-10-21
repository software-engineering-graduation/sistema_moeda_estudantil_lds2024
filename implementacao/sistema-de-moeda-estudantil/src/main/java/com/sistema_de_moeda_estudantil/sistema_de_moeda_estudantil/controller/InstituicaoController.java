package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.instituicao.InstituicaoDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.service.InstituicaoService;

@RestController
@RequestMapping("/instituicoes")
public class InstituicaoController {

    private final InstituicaoService instituicaoService;

    public InstituicaoController(InstituicaoService instituicaoService) {
        this.instituicaoService = instituicaoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstituicaoDTO> getById(@PathVariable int id) {
        InstituicaoDTO instituicaoDTO = instituicaoService.getById(id);
        return ResponseEntity.ok(instituicaoDTO);
    }

    @GetMapping
    public ResponseEntity<List<InstituicaoDTO>> getAll() {
        List<InstituicaoDTO> instituicoes = instituicaoService.getAll();
        return ResponseEntity.ok(instituicoes);
    }
}
