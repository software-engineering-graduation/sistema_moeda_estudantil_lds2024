package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.controller;

import org.springframework.web.bind.annotation.*;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Empresa.EmpresaCreate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Empresa.EmpresaDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.service.EmpresaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @GetMapping
    public List<EmpresaDTO> obterTodasEmpresas() {
        return empresaService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaDTO> obterEmpresaPorId(@PathVariable int id) {
        Optional<EmpresaDTO> empresaDTO = empresaService.buscarPorId(id);
        return empresaDTO.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EmpresaDTO> criarEmpresa(@RequestBody EmpresaCreate empresaCreate) {
        EmpresaDTO empresaDTO = empresaService.salvar(empresaCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(empresaDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpresaDTO> atualizarEmpresa(@PathVariable int id, @RequestBody EmpresaCreate empresaCreate) {
        Optional<EmpresaDTO> empresaAtualizada = empresaService.atualizar(id, empresaCreate);
        return empresaAtualizada.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEmpresa(@PathVariable int id) {
        empresaService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
