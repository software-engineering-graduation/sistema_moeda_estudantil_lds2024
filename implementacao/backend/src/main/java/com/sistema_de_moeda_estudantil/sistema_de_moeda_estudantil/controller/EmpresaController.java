package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.SenhaDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Empresa.EmpresaCreate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Empresa.EmpresaDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.FuncionarioEmpresa.FuncionarioEmpresaCreate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.FuncionarioEmpresa.FuncionarioEmpresaDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Vantagem.VantagemCreate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Vantagem.VantagemDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.service.EmpresaService;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.service.FuncionarioEmpresaService;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.service.VantagemService;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private FuncionarioEmpresaService funcionarioEmpresaService;

    @Autowired
    private VantagemService vantagemService;

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

    @GetMapping("/{empresaId}/funcionarios")
    public ResponseEntity<List<FuncionarioEmpresaDTO>> obterFuncionariosPorEmpresaId(@PathVariable int empresaId) {
        List<FuncionarioEmpresaDTO> funcionarios = funcionarioEmpresaService.getByEmpresaId(empresaId);
        return ResponseEntity.ok(funcionarios);
    }

    @PostMapping("/{empresaId}/funcionarios")
    public ResponseEntity<FuncionarioEmpresaDTO> criarFuncionario(@PathVariable int empresaId,
            @RequestBody FuncionarioEmpresaCreate funcionarioCreate) {
        FuncionarioEmpresaDTO funcionarioCriado = funcionarioEmpresaService.create(empresaId, funcionarioCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(funcionarioCriado);
    }

    @PutMapping("/{empresaId}/funcionarios/{funcionarioId}")
    public ResponseEntity<FuncionarioEmpresaDTO> atualizarFuncionario(@PathVariable int empresaId,
            @PathVariable int funcionarioId, @RequestBody FuncionarioEmpresaDTO funcionarioUpdate) {
        FuncionarioEmpresaDTO funcionarioAtualizado = funcionarioEmpresaService.update(empresaId, funcionarioId,
                funcionarioUpdate);
        return ResponseEntity.ok(funcionarioAtualizado);
    }

    @GetMapping("/{empresaId}/funcionarios/{funcionarioId}")
    public ResponseEntity<FuncionarioEmpresaDTO> obterFuncionarioPorId(@PathVariable int empresaId,
            @PathVariable int funcionarioId) {
        Optional<FuncionarioEmpresaDTO> funcionario = funcionarioEmpresaService.getById(empresaId, funcionarioId);
        return funcionario.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{empresaId}/funcionarios/{funcionarioId}/senha")
    public ResponseEntity<FuncionarioEmpresaDTO> atualizarSenha(@PathVariable int empresaId,
            @PathVariable int funcionarioId, @RequestBody SenhaDTO senhaDTO) {
        Optional<FuncionarioEmpresaDTO> funcionarioAtualizado = funcionarioEmpresaService.atualizarSenha(empresaId,
                funcionarioId, senhaDTO.getSenhaAntiga(),
                senhaDTO.getNovaSenha());
        return funcionarioAtualizado.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{empresaId}/funcionarios/{id}")
    public ResponseEntity<Void> deletarFuncionario(@PathVariable int empresaId, @PathVariable int id) {
        funcionarioEmpresaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{empresaId}/vantagens")
    public ResponseEntity<List<VantagemDTO>> obterVantagensPorEmpresaId(@PathVariable int empresaId) {
        List<VantagemDTO> vantagens = vantagemService.getByEmpresaId(empresaId);
        return ResponseEntity.ok(vantagens);
    }

    @PostMapping("/{empresaId}/vantagens")
    public ResponseEntity<VantagemDTO> criarVantagem(@PathVariable int empresaId,
            @RequestBody VantagemCreate vantagemCreate) {
        VantagemDTO vantagemCriada = vantagemService.create(empresaId, vantagemCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(vantagemCriada);
    }

    @PutMapping("/{empresaId}/vantagens/{vantagemId}")
    public ResponseEntity<VantagemDTO> atualizarVantagem(@PathVariable int empresaId,
            @PathVariable int vantagemId, @RequestBody VantagemDTO vantagemUpdate) {
        VantagemDTO vantagemAtualizada = vantagemService.update(empresaId, vantagemId, vantagemUpdate);
        return ResponseEntity.ok(vantagemAtualizada);
    }

    @GetMapping("/{empresaId}/vantagens/{vantagemId}")
    public ResponseEntity<VantagemDTO> obterVantagemPorId(@PathVariable int empresaId,
            @PathVariable int vantagemId) {
        Optional<VantagemDTO> vantagem = vantagemService.getById(empresaId, vantagemId);
        return vantagem.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{empresaId}/vantagens/{vantagemId}")
    public ResponseEntity<Void> deletarVantagem(@PathVariable int empresaId, @PathVariable int vantagemId) {
        vantagemService.delete(empresaId, vantagemId);
        return ResponseEntity.noContent().build();
    }
}
