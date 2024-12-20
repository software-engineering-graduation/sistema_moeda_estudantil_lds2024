package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.instituicao;

import java.util.List;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.professor.ProfessorCreate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstituicaoCreate {
    private String nome;
    private String endereco;
    private List<ProfessorCreate> professores;
}
