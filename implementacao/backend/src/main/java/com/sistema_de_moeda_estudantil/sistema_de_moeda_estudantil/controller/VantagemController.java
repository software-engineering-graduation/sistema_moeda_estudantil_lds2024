package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Vantagem.CupomResgateDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Vantagem.VantagemDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.service.VantagemService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/vantagens")
public class VantagemController {
    private final VantagemService vantagemService;

    @PostMapping("/resgatar")
    public ResponseEntity<CupomResgateDTO> resgatarVantagem(@RequestParam Long alunoId, @RequestParam Long vantagemId) {
        return ResponseEntity.ok(vantagemService.resgatarVantagem(alunoId, vantagemId));
    }

    @GetMapping
    public ResponseEntity<List<VantagemDTO>> getByAllAvailable() {
        return ResponseEntity.ok(vantagemService.getByAllAvailable());
    }

    @GetMapping("/byUser/{userId}")
    public ResponseEntity<List<CupomResgateDTO>> getCuponsByUser(Authentication authentication,
            @PathVariable Long userId) {
        return ResponseEntity.ok(vantagemService.getCuponsByUser(authentication, userId));
    }
}
