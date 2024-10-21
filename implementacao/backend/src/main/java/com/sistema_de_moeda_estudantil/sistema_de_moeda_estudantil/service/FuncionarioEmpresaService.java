package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.FuncionarioEmpresa.FuncionarioEmpresaCreate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.FuncionarioEmpresa.FuncionarioEmpresaDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Empresa;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.FuncionarioEmpresa;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.mapper.FuncionarioEmpresaMapper;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.EmpresaRepository;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.FuncionarioEmpresaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class FuncionarioEmpresaService {

    private FuncionarioEmpresaRepository funcionarioEmpresaRepository;
    private EmpresaRepository empresaRepository;

    @Autowired
    private FuncionarioEmpresaMapper mapper;

    public FuncionarioEmpresaService(FuncionarioEmpresaRepository funcionarioEmpresaRepository,
            EmpresaRepository empresaRepository) {
        this.funcionarioEmpresaRepository = funcionarioEmpresaRepository;
        this.empresaRepository = empresaRepository;
    }

    public FuncionarioEmpresaDTO getById(int id) {
        FuncionarioEmpresa funcionario = funcionarioEmpresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Funcionário não encontrado com id: " + id));
        return mapper.toDTO(funcionario);
    }

    public List<FuncionarioEmpresaDTO> getAll() {
        List<FuncionarioEmpresa> funcionarios = funcionarioEmpresaRepository.findAll();
        return funcionarios.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<FuncionarioEmpresaDTO> getByEmpresaId(int empresaId) {
        List<FuncionarioEmpresa> funcionarios = funcionarioEmpresaRepository.findByEmpresaId(empresaId);
        return funcionarios.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public FuncionarioEmpresaDTO create(FuncionarioEmpresaCreate createDTO) {
        Empresa empresa = empresaRepository.findById(createDTO.getEmpresaId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Empresa não encontrada com id: " + createDTO.getEmpresaId()));
        FuncionarioEmpresa funcionario = mapper.toEntity(createDTO);
        funcionario.setEmpresa(empresa);
        return mapper.toDTO(funcionarioEmpresaRepository.save(funcionario));
    }

    public FuncionarioEmpresaDTO update(int empresaId, int funcionarioId, FuncionarioEmpresaDTO updateDTO) {
        FuncionarioEmpresa funcionario = funcionarioEmpresaRepository.findById(funcionarioId)
                .orElseThrow(() -> new EntityNotFoundException("Funcionário não encontrado com id: " + funcionarioId));
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Empresa não encontrada com id: " + empresaId));
        mapper.updateEntityFromDto(updateDTO, funcionario);
        funcionario.setEmpresa(empresa);
        return mapper.toDTO(funcionarioEmpresaRepository.save(funcionario));
    }

    public void delete(int id) {
        FuncionarioEmpresa funcionario = funcionarioEmpresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Funcionário não encontrado com id: " + id));
        funcionarioEmpresaRepository.delete(funcionario);
    }

}
