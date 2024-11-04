package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.transacao.TransacaoCreate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.transacao.TransacaoDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Transacao;

@Mapper(componentModel = "spring")
public interface TransacaoMapper {

    TransacaoMapper INSTANCE = Mappers.getMapper(TransacaoMapper.class);


    TransacaoDTO toDTO(Transacao transacao);


    @Mapping(target = "data", ignore = true)
    Transacao toEntity(TransacaoDTO transacaoDTO);

    @Mapping(target = "id", ignore = true)

    @Mapping(target = "data", ignore = true)
    @Mapping(target = "origem", ignore = true)
    @Mapping(target = "destino", ignore = true)
    Transacao toEntity(TransacaoCreate transacaoCreate);
}
