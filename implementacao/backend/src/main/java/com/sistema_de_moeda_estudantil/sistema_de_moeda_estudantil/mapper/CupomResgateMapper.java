package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Vantagem.CupomResgateDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.CupomResgate;

@Mapper
public interface CupomResgateMapper {
    CupomResgateMapper INSTANCE = Mappers.getMapper(CupomResgateMapper.class);
    
    CupomResgateDTO toDTO(CupomResgate vantagem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    void updateEntityFromDTO(CupomResgateDTO vantagemDTO, @MappingTarget CupomResgate vantagem);
}
