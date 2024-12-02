package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.instituicao.InstituicaoCreate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.instituicao.InstituicaoDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.instituicao.InstituicaoUpdate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.professor.ProfessorCreate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.professor.ProfessorDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Instituicao;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Professor;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Semestre;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.mapper.InstituicaoMapper;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.mapper.ProfessorMapper;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.InstituicaoRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InstituicaoService {

    private final InstituicaoRepository instituicaoRepository;
    private final InstituicaoMapper instituicaoMapper;
    private final SemestreService semestreService;
    private final PasswordEncoder passwordEncoder;

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
        instituicao.setProfessores(new ArrayList<>());
        for (ProfessorCreate professorCreate : createDTO.getProfessores()) {
            Professor professor = ProfessorMapper.toEntity(professorCreate);
            if (professor.getSenha() == null) {
                throw new IllegalArgumentException("rawPassword cannot be null");
            }
            instituicao.getProfessores().add(professor);
            professor.setSenha(passwordEncoder.encode(professor.getSenha()));
            professor.setInstituicao(instituicao);
        }
        Instituicao savedInstituicao = instituicaoRepository.save(instituicao);
        return instituicaoMapper.toDTO(savedInstituicao);
    }

    public InstituicaoDTO update(Long id, InstituicaoUpdate instituicaoDto) {
        Instituicao instituicao = instituicaoRepository.findById(id)
                .orElseThrow();
        instituicao.setEndereco(instituicaoDto.getEndereco());
        instituicao.setNome(instituicaoDto.getNome());
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

        Semestre semestre = semestreService.novo(instituicao);
        
        instituicao.getSemestres().add(semestre);
        adicionarSaldoDocentes(instituicaoId, Double.valueOf(1000));
        instituicaoRepository.save(instituicao);
    }

    public void adicionarSaldoDocentes(Long instituicaoId, Double saldo) {
        Instituicao instituicao = instituicaoRepository.getReferenceById(instituicaoId);
        instituicao.getProfessores().forEach(professor -> professor.setSaldoMoedas(professor.getSaldoMoedas() + saldo));
        instituicaoRepository.save(instituicao);
    }

    public List<ProfessorDTO> getProfessores(Long instituicaoId) {
        Instituicao instituicao = instituicaoRepository.findById(instituicaoId)
            .orElseThrow(EntityNotFoundException::new);
        
        return instituicao.getProfessores().stream()
            .map(professor -> {
                ProfessorDTO dto = new ProfessorDTO();
                dto.setId(professor.getId());
                dto.setNome(professor.getNome());
                dto.setEmail(professor.getEmail());
                dto.setSaldoMoedas(professor.getSaldoMoedas());
                return dto;
            })
            .collect(Collectors.toList());
    }
}
