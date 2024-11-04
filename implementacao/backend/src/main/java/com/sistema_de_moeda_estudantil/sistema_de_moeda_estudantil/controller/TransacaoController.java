package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.transacao.TransacaoCreate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.transacao.TransacaoDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.service.TransacaoService;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @PostMapping("/transferir")
    public ResponseEntity<TransacaoDTO> transferir(@RequestBody @Validated TransacaoCreate createDTO) {
        TransacaoDTO transacaoDTO = transacaoService.enviarMoedas(createDTO);
        return ResponseEntity.ok(transacaoDTO);
    }

    @PostMapping("/reabastecer")
    public ResponseEntity<Void> reabastercerSaldo() {
        transacaoService.reabastecerSaldoProfessores();
        return ResponseEntity.ok().build();
    }
    
}
