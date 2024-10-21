package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.controller;

import org.springframework.web.bind.annotation.*;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Aluno.AlunoCreate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Aluno.AlunoDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.service.AlunoService;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @GetMapping
    public List<AlunoDTO> obterTodosAlunos() {
        return alunoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoDTO> obterAlunoPorId(@PathVariable int id) {
        Optional<AlunoDTO> alunoDTO = alunoService.buscarPorId(id);
        return alunoDTO.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AlunoDTO> criarAluno(@Valid @RequestBody AlunoCreate alunoCreate) {
        AlunoDTO alunoDTO = alunoService.salvar(alunoCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(alunoDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoDTO> atualizarAluno(@PathVariable int id, @Valid @RequestBody AlunoCreate alunoCreate) {
        Optional<AlunoDTO> alunoAtualizado = alunoService.atualizar(id, alunoCreate);
        return alunoAtualizado.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAluno(@PathVariable int id) {
        alunoService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
