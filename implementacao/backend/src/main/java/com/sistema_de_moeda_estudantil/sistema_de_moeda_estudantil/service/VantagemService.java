package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Vantagem.VantagemCreate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Vantagem.VantagemDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Empresa;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Vantagem;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.mapper.VantagemMapper;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.EmpresaRepository;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.VantagemRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class VantagemService {

    @Autowired
    private VantagemRepository vantagemRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    private final VantagemMapper vantagemMapper = VantagemMapper.INSTANCE;

    public List<VantagemDTO> getByEmpresaId(int empresaId) {
        return vantagemRepository.findByEmpresaId(empresaId).stream()
                .map(vantagemMapper::toDTO)
                .collect(Collectors.toList());
    }

    public VantagemDTO create(int empresaId, VantagemCreate vantagemCreate) {
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada"));

        Vantagem vantagem = vantagemMapper.toEntity(vantagemCreate);
        vantagem.setEmpresa(empresa);

        return vantagemMapper.toDTO(vantagemRepository.save(vantagem));
    }

    public VantagemDTO update(int empresaId, int vantagemId, VantagemDTO vantagemUpdate) {
        Vantagem vantagem = vantagemRepository.findByIdAndEmpresaId(vantagemId, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Vantagem não encontrada"));

        vantagemMapper.updateEntityFromDTO(vantagemUpdate, vantagem);

        return vantagemMapper.toDTO(vantagemRepository.save(vantagem));
    }

    public Optional<VantagemDTO> getById(int empresaId, int vantagemId) {
        return vantagemRepository.findByIdAndEmpresaId(vantagemId, empresaId)
                .map(vantagemMapper::toDTO);
    }

    public void delete(int empresaId, int vantagemId) {
        Vantagem vantagem = vantagemRepository.findByIdAndEmpresaId(vantagemId, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Vantagem não encontrada"));
        vantagemRepository.delete(vantagem);
    }
}