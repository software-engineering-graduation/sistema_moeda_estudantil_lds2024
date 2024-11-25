package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.mapper;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.professor.ProfessorCreate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.professor.ProfessorDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Professor;

@Component
public class ProfessorMapper {

    static InstituicaoMapper instituicaoMapper = InstituicaoMapper.INSTANCE;
    
    @Named("professorToProfessorDTO")
        public static ProfessorDTO toDTO(Professor professor) {
            ProfessorDTO dto = new ProfessorDTO();
            dto.setId(professor.getId());
            dto.setNome(professor.getNome());
            dto.setDepartamento(professor.getDepartamento());
            dto.setInstituicao(instituicaoMapper.toDTO(professor.getInstituicao()));
            dto.setSaldo(professor.getSaldoMoedas());
        return dto;
    }

    public static Professor toEntity(ProfessorCreate createDTO) {
        Professor professor = new Professor();
        professor.setNome(createDTO.getNome());
        professor.setDepartamento(createDTO.getDepartamento());
        return professor;
    }
}
