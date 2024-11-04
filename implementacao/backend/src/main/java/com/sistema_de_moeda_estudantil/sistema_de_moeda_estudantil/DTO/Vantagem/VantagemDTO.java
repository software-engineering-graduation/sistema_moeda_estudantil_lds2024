package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Vantagem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VantagemDTO {
    private Long id;
    private String descricao;
    private String foto;
    private Long custoMoedas;
    private Long quantidadeDisponivel;
}
