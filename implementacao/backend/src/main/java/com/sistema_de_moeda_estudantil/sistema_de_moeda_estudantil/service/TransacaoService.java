package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.service;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Transacao;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Usuario;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.mapper.TransacaoMapper;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.transacao.TransacaoCreate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.transacao.TransacaoDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Professor;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.TransacaoRepository;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired TransacaoMapper  transacaoMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public TransacaoDTO enviarMoedas(TransacaoCreate transacaoCreate) {
        Transacao transacao = transacaoMapper.toEntity(transacaoCreate);
        Usuario origem = usuarioRepository.findById(transacaoCreate.getOrigem()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Usuario destino = usuarioRepository.findById(transacaoCreate.getDestino()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Double valor = transacao.getValor();
        if (origem.getSaldoMoedas() >= valor) {
            origem.setSaldoMoedas(origem.getSaldoMoedas() - valor);
            destino.setSaldoMoedas(destino.getSaldoMoedas() + valor);
        } else {
            throw new RuntimeException("Saldo insuficiente");
        }
        usuarioRepository.save(origem);
        usuarioRepository.save(destino);
        transacao.setData(LocalDateTime.now());
        transacaoRepository.save(transacao);

        return transacaoMapper.toDTO(transacao);
    }

    public void reabastecerSaldoProfessores() {
        for (Usuario usuario : usuarioRepository.findAll()) {
            if (usuario instanceof Professor) {
                Professor professor = (Professor) usuario;
                professor.setSaldoMoedas(professor.getSaldoMoedas() + 1000.0);
                usuarioRepository.save(professor);
            }
        }
    }
}
