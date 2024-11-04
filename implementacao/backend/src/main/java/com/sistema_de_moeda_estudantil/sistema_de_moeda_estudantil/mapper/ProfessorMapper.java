package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.mapper;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.professor.ProfessorCreate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.professor.ProfessorDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Professor;

public class ProfessorMapper {

    public static ProfessorDTO toDTO(Professor professor) {
        ProfessorDTO dto = new ProfessorDTO();
        dto.setId(professor.getId());
        dto.setNome(professor.getNome());
        dto.setDepartamento(professor.getDepartamento());
        return dto;
    }

    public static Professor toEntity(ProfessorCreate createDTO) {
        Professor professor = new Professor();
        professor.setNome(createDTO.getNome());
        professor.setDepartamento(createDTO.getDepartamento());
        return professor;
    }
}
