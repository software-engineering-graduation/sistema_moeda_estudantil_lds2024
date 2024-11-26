package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.transacao.TransacaoCreate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.transacao.TransacaoDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Transacao;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Usuario;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.enums.TipoUsuario;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.mapper.TransacaoMapper;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.ProfessorRepository;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.TransacaoRepository;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.UsuarioRepository;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private TransacaoMapper transacaoMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    public List<TransacaoDTO> listarTransacoes(Authentication authentication) {
        Usuario usuario = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<Transacao> transacoes;

        // Se for admin, retorna todas as transações
        if (usuario.getTipo() == TipoUsuario.ADMIN) {
            transacoes = transacaoRepository.findAll();
        } else {
            // Se não for admin, retorna apenas as transações do usuário
            transacoes = transacaoRepository.findByOrigemIdOrDestinoId(usuario.getId(), usuario.getId());
        }

        return transacoes.stream()
                .map(transacaoMapper::toDTO)
                .sorted((t1, t2) -> t2.getData().compareTo(t1.getData()))
                .collect(Collectors.toList());
    }

    public TransacaoDTO enviarMoedas(TransacaoCreate transacaoCreate, Authentication authentication) {
        Transacao transacao = transacaoMapper.toEntity(transacaoCreate);
        Usuario origem = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        if(!TipoUsuario.PROFESSOR.equals(origem.getTipo())) {
            throw new RuntimeException("Apenas professores podem transferir moedas");
        }
        Usuario destino = usuarioRepository.findById(transacaoCreate.getDestino())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Double valor = transacao.getValor();
        if (origem.getSaldoMoedas() >= valor) {
            origem.setSaldoMoedas(origem.getSaldoMoedas() - valor);
            destino.setSaldoMoedas(destino.getSaldoMoedas() + valor);
        } else {
            throw new RuntimeException("Saldo insuficiente");
        }
        transacao.setData(LocalDateTime.now());
        transacao.setOrigem(origem);
        transacao.setDestino(destino);
        transacaoRepository.save(transacao);        

        return transacaoMapper.toDTO(transacao);
    }

    public void reabastecerSaldoProfessores() {
        professorRepository.reabastecerSaldo(1000.0);
    }
}
