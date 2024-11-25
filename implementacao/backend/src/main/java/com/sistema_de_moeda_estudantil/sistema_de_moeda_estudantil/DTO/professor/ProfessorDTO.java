package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.professor;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.instituicao.InstituicaoDTO;

import lombok.Data;

@Data
public class ProfessorDTO {
    private Long id;
    private String nome;
    private String departamento;
    private InstituicaoDTO instituicao;
    private Double saldo;
}
