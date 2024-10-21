package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.instituicao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstituicaoDTO {
    private int id;
    private String nome;
    private String endereco;
}