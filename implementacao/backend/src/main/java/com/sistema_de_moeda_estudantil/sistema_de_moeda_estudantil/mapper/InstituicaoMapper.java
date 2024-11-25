package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.mapper;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.instituicao.InstituicaoCreate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.instituicao.InstituicaoDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Instituicao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class InstituicaoMapper {
    public static final InstituicaoMapper INSTANCE = Mappers.getMapper(InstituicaoMapper.class);

    public abstract InstituicaoDTO toDTO(Instituicao instituicao);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "professores", ignore = true)
    @Mapping(target = "semestres", ignore = true)
    public abstract Instituicao toEntity(InstituicaoCreate instituicaoCreate);

    @Mapping(target = "professores", ignore = true)
    public abstract Instituicao toEntity(InstituicaoDTO instituicaoDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "professores", ignore = true)
    public abstract void updateEntityFromDto(InstituicaoDTO instituicaoDto, @MappingTarget Instituicao instituicao);
}