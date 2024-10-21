package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Aluno.AlunoCreate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Aluno.AlunoDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Aluno;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AlunoMapper {

    AlunoMapper INSTANCE = Mappers.getMapper(AlunoMapper.class);

    AlunoDTO toDTO(Aluno aluno);

    @Mapping(target = "instituicao", ignore = true)
    @Mapping(target = "saldoMoedas", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tipo", ignore = true)
    Aluno toEntity(AlunoCreate alunoCreate);

    @Mapping(target = "saldoMoedas", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tipo", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "instituicao", ignore = true)
    void updateEntityFromDto(AlunoCreate alunoCreate, @MappingTarget Aluno aluno);
}

