package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.professor;

import lombok.Data;

@Data
public class ProfessorCreate {
    private String nome;
    private String email;
    private String senha;
    private String departamento;
}
