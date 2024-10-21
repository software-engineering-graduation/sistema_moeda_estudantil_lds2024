package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Vantagem.VantagemCreate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Vantagem.VantagemDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Vantagem;

@Mapper
public interface VantagemMapper {
    VantagemMapper INSTANCE = Mappers.getMapper(VantagemMapper.class);

    VantagemDTO toDTO(Vantagem vantagem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    Vantagem toEntity(VantagemCreate vantagemCreate);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    void updateEntityFromDTO(VantagemDTO vantagemDTO, @MappingTarget Vantagem vantagem);
}
