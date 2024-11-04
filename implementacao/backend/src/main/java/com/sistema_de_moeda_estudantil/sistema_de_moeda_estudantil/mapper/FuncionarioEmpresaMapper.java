package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.FuncionarioEmpresa.FuncionarioEmpresaCreate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.FuncionarioEmpresa.FuncionarioEmpresaDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.FuncionarioEmpresa;

@Mapper(componentModel = "spring", uses = {EmpresaMapper.class})
public interface FuncionarioEmpresaMapper {

    FuncionarioEmpresaMapper INSTANCE = Mappers.getMapper(FuncionarioEmpresaMapper.class);

    
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "tipo", ignore = true)
    @Mapping(target = "saldoMoedas", ignore = true)
    FuncionarioEmpresa toEntity(FuncionarioEmpresaDTO funcionarioEmpresaDTO);

    FuncionarioEmpresaDTO toDTO(FuncionarioEmpresa funcionarioEmpresa);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tipo", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "saldoMoedas", ignore = true)
    FuncionarioEmpresa toEntity(FuncionarioEmpresaCreate funcionarioEmpresaCreate);

    @Mapping(target = "empresa", ignore = true)
    @Mapping(target = "tipo", ignore = true)
    @Mapping(target = "saldoMoedas", ignore = true)
    void updateEntityFromDto(FuncionarioEmpresaDTO funcionarioEmpresaDto, @MappingTarget FuncionarioEmpresa funcionarioEmpresa);
}