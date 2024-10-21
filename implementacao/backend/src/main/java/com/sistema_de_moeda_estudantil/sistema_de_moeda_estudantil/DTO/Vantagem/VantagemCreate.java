package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Vantagem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VantagemCreate {
    private String descricao;
    private String foto;
    private int custoMoedas;
    private int quantidadeDisponivel;
}