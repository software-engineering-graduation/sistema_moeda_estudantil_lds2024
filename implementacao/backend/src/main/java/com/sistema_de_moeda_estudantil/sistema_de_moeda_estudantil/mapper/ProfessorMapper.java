package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.professor.ProfessorCreate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.professor.ProfessorDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Professor;

@Mapper(componentModel = "spring")
public interface ProfessorMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "instituicao", ignore = true)
    @Mapping(target = "transacoes", ignore = true)
    Professor toEntity(ProfessorCreate professorCreate);

    @Mapping(target = "instituicao", ignore = true)
    @Mapping(target = "transacoes", ignore = true)
    @Mapping(target = "tipo", ignore = true)
    @Mapping(target = "saldoMoedas", ignore = true)
    @Mapping(target = "senha", ignore = true)
    Professor toEntity(ProfessorDTO professorDTO);

    ProfessorDTO toDTO(Professor professor);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "instituicao", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "transacoes", ignore = true)
    void updateEntityFromDto(ProfessorDTO professorDTO, @MappingTarget Professor professor);
}
