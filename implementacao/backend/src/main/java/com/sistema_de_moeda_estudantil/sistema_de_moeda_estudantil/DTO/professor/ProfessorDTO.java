package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.professor;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Instituicao;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.enums.TipoUsuario;

import lombok.Data;

@Data
public class ProfessorDTO {
    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private Double saldoMoedas;
    private TipoUsuario tipo;
    private String departamento;
    private Instituicao instituicao;
}
