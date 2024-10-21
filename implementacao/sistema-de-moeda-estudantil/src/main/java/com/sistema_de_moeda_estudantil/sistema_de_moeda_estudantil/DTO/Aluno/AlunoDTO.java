package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Aluno;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Instituicao;

import lombok.Data;

@Data
public class AlunoDTO {
    private int id;
    private String nome;
    private String email;
    private String cpf;
    private String rg;
    private String endereco;
    private String curso;
    private int saldoMoedas;
    private Instituicao instituicao;
}

