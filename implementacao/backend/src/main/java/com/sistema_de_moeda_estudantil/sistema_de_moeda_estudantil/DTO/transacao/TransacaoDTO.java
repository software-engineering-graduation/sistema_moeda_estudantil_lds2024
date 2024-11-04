package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.transacao;

import java.time.LocalDateTime;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Usuario;

import lombok.Data;

@Data
public class TransacaoDTO {
    private Long id;
    private Usuario origem;
    private Usuario destino;
    private Double valor;
    private LocalDateTime data;
    private String mensagem;
}
