package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.transacao;

import lombok.Data;

@Data
public class TransacaoCreate {
    private Long origem;
    private Long destino;
    private Double valor;
    private String mensagem;
}
