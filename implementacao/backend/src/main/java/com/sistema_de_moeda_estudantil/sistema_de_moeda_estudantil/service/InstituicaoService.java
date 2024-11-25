package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.service;


import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.instituicao.InstituicaoCreate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.instituicao.InstituicaoDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Instituicao;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Semestre;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.mapper.InstituicaoMapper;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.InstituicaoRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstituicaoService {

    private final InstituicaoRepository instituicaoRepository;
    private final InstituicaoMapper instituicaoMapper;
    private final SemestreService semestreService;


    public InstituicaoService(InstituicaoRepository instituicaoRepository, InstituicaoMapper instituicaoMapper, SemestreService semestreService) {
        this.instituicaoRepository = instituicaoRepository;
        this.instituicaoMapper = instituicaoMapper;
        this.semestreService = semestreService;
    }

    public InstituicaoDTO getById(Long id) {
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

    public InstituicaoDTO create(InstituicaoCreate createDTO) {
        Instituicao instituicao = instituicaoMapper.toEntity(createDTO);
        Instituicao savedInstituicao = instituicaoRepository.save(instituicao);
        return instituicaoMapper.toDTO(savedInstituicao);
    }

    public InstituicaoDTO update(Long id, InstituicaoDTO instituicaoDto) {
        Instituicao instituicao = instituicaoRepository.findById(id)
                .orElseThrow();
        instituicaoMapper.updateEntityFromDto(instituicaoDto, instituicao);
        Instituicao updatedInstituicao = instituicaoRepository.save(instituicao);
        return instituicaoMapper.toDTO(updatedInstituicao);
    }

    public void delete(Long id) {
        Instituicao instituicao = instituicaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Instituição não encontrada com id: " + id));
        instituicaoRepository.delete(instituicao);
    }

    public void novoSemestre(Long instituicaoId) {
        Instituicao instituicao = instituicaoRepository.getReferenceById(instituicaoId);

        if(instituicao.getSemestres().size() > 0){        
            instituicao.getSemestres().getLast().setAtivo(false);
            instituicao.getSemestres().getLast().setDataFim(LocalDateTime.now());
        }

        Semestre semestre = semestreService.novo();
        
        instituicao.getSemestres().add(semestre);
        adicionarSaldoDocentes(instituicaoId, Double.valueOf(1000));
        instituicaoRepository.save(instituicao);
    }

    public void adicionarSaldoDocentes(Long instituicaoId, Double saldo) {
        Instituicao instituicao = instituicaoRepository.getReferenceById(instituicaoId);
        instituicao.getProfessores().forEach(professor -> professor.setSaldoMoedas(professor.getSaldoMoedas() + saldo));
        instituicaoRepository.save(instituicao);
    }
}
