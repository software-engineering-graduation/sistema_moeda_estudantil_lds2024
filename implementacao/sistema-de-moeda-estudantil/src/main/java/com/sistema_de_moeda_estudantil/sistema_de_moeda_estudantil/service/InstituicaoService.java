package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.service;


import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.instituicao.InstituicaoDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Instituicao;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.mapper.InstituicaoMapper;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.InstituicaoRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstituicaoService {

    private final InstituicaoRepository instituicaoRepository;
    private final InstituicaoMapper instituicaoMapper;


    public InstituicaoService(InstituicaoRepository instituicaoRepository, InstituicaoMapper instituicaoMapper) {
        this.instituicaoRepository = instituicaoRepository;
        this.instituicaoMapper = instituicaoMapper;
    }

    public InstituicaoDTO getById(int id) {
        Instituicao instituicao = instituicaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Instituicao não encontrada com id: " + id));
        return instituicaoMapper.toDTO(instituicao);
    }

    public List<InstituicaoDTO> getAll() {
        List<Instituicao> instituicoes = instituicaoRepository.findAll();
        return instituicoes.stream()
                .map(instituicaoMapper::toDTO)
                .collect(Collectors.toList());
    }
}
