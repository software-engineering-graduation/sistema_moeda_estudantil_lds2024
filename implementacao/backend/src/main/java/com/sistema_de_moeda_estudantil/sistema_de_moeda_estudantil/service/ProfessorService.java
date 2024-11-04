package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.professor.ProfessorCreate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.professor.ProfessorDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.transacao.TransacaoDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.mapper.ProfessorMapper;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.mapper.TransacaoMapper;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Professor;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.ProfessorRepository;

@Service
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    @Autowired
    private TransacaoMapper transacaoMapper;

    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    @Transactional(readOnly = true)
    public ProfessorDTO getById(Long id) {
        Professor professor = professorRepository.findById(id).orElseThrow(() -> new RuntimeException("Professor not found"));
        return ProfessorMapper.toDTO(professor);
    }

    @Transactional(readOnly = true)
    public List<ProfessorDTO> getAll() {
        return professorRepository.findAll().stream().map(ProfessorMapper::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public ProfessorDTO create(ProfessorCreate createDTO) {
        Professor professor = ProfessorMapper.toEntity(createDTO);
        professor = professorRepository.save(professor);
        return ProfessorMapper.toDTO(professor);
    }

    @Transactional
    public ProfessorDTO update(Long id, ProfessorDTO updateDTO) {
        Professor professor = professorRepository.findById(id).orElseThrow(() -> new RuntimeException("Professor not found"));
        professor.setNome(updateDTO.getNome());
        professor.setDepartamento(updateDTO.getDepartamento());
        professor = professorRepository.save(professor);
        return ProfessorMapper.toDTO(professor);
    }

    @Transactional
    public void delete(Long id) {
        professorRepository.deleteById(id);
    }

    public List<TransacaoDTO> getTransacoes(Long id) {
        Professor professor = professorRepository.findById(id).orElseThrow(() -> new RuntimeException("Professor not found"));
        return professor.getTransacoes().stream().map(transacaoMapper::toDTO).collect(Collectors.toList());
    }
}
