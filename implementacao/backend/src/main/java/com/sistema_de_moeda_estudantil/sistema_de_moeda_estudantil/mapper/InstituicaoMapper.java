package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.instituicao.InstituicaoCreate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.instituicao.InstituicaoDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Instituicao;

@Mapper(componentModel = "spring")
public interface InstituicaoMapper {
   InstituicaoMapper INSTANCE = Mappers.getMapper(InstituicaoMapper.class);

    InstituicaoDTO toDTO(Instituicao instituicao);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "alunos", ignore = true)
    @Mapping(target = "professores", ignore = true)
    Instituicao toEntity(InstituicaoCreate instituicaoCreate);

    @Mapping(target = "alunos", ignore = true)
    @Mapping(target = "professores", ignore = true)
    Instituicao toEntity(InstituicaoDTO instituicaoDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "alunos", ignore = true)
    @Mapping(target = "professores", ignore = true)
    void updateEntityFromDto(InstituicaoDTO instituicaoDto, @MappingTarget Instituicao instituicao);
}
