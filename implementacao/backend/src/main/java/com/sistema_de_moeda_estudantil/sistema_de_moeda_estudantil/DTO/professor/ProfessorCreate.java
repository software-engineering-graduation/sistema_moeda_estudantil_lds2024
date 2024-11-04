package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.professor;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.enums.TipoUsuario;

import lombok.Data;

@Data
public class ProfessorCreate {
    private String nome;
    private String email;
    private String cpf;
    private Double saldoMoedas;
    private TipoUsuario tipo = TipoUsuario.PROFESSOR;
    private String departamento;
    private Long instituicaoId;
    private String senha;
}
