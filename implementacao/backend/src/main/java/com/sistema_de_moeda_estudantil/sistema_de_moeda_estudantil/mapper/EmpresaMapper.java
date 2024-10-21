package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Empresa.EmpresaCreate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Empresa.EmpresaDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Empresa;

@Mapper(componentModel = "spring")
public interface EmpresaMapper {

    EmpresaMapper INSTANCE = Mappers.getMapper(EmpresaMapper.class);

    EmpresaDTO toDTO(Empresa empresa);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tipo", ignore = true)
    @Mapping(target = "saldoMoedas", ignore = true)
    @Mapping(target = "vantagens", ignore = true)
    Empresa toEntity(EmpresaCreate empresaCreate);

    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "tipo", ignore = true)
    @Mapping(target = "vantagens", ignore = true)
    Empresa toEntity(EmpresaDTO empresaDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tipo", ignore = true)
    @Mapping(target = "saldoMoedas", ignore = true)
    @Mapping(target = "vantagens", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(EmpresaCreate empresaCreate, @MappingTarget Empresa empresa);
}
