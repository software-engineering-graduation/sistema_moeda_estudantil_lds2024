package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.FuncionarioEmpresa.FuncionarioEmpresaCreate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.FuncionarioEmpresa.FuncionarioEmpresaDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Aluno;
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

    public FuncionarioEmpresaDTO getById(Long id) {
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

    public List<FuncionarioEmpresaDTO> getByEmpresaId(Long empresaId) {
        List<FuncionarioEmpresa> funcionarios = funcionarioEmpresaRepository.findByEmpresaId(empresaId);
        return funcionarios.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public FuncionarioEmpresaDTO create(Long empresaId, FuncionarioEmpresaCreate createDTO) {
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Empresa não encontrada com id: " + createDTO.getEmpresaId()));
        FuncionarioEmpresa funcionario = mapper.toEntity(createDTO);
        funcionario.setEmpresa(empresa);
        return mapper.toDTO(funcionarioEmpresaRepository.save(funcionario));
    }

    public FuncionarioEmpresaDTO update(Long empresaId, Long funcionarioId, FuncionarioEmpresaDTO updateDTO) {
        FuncionarioEmpresa funcionario = funcionarioEmpresaRepository.findById(funcionarioId)
                .orElseThrow(() -> new EntityNotFoundException("Funcionário não encontrado com id: " + funcionarioId));
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Empresa não encontrada com id: " + empresaId));
        mapper.updateEntityFromDto(updateDTO, funcionario);
        funcionario.setEmpresa(empresa);
        return mapper.toDTO(funcionarioEmpresaRepository.save(funcionario));
    }

    public void delete(Long id) {
        FuncionarioEmpresa funcionario = funcionarioEmpresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Funcionário não encontrado com id: " + id));
        funcionarioEmpresaRepository.delete(funcionario);
    }

    public Optional<FuncionarioEmpresaDTO> getById(Long empresaId, Long funcionarioId) {
        Optional<FuncionarioEmpresa> funcionario = funcionarioEmpresaRepository.findByIdAndEmpresaId(funcionarioId, empresaId);
        return funcionario.map(mapper::toDTO);
    }

    public Optional<FuncionarioEmpresaDTO> atualizarSenha(Long empresaId, Long funcionarioId, String senhaAntiga, String novaSenha) {
        Optional<FuncionarioEmpresa> funcinarioExistente = funcionarioEmpresaRepository.findById(funcionarioId);

        if (funcinarioExistente.isPresent()) {
            
            if(funcinarioExistente.get().getEmpresa() == null || funcinarioExistente.get().getEmpresa().getId() != empresaId){
                throw new IllegalArgumentException("Empresa inválida");
            }

            FuncionarioEmpresa funcionario = funcinarioExistente.get();

            if (funcionario.getSenha().equals(senhaAntiga)) {
                funcionario.setSenha(novaSenha);
                FuncionarioEmpresa updatedFuncionario = funcionarioEmpresaRepository.save(funcionario);
                return Optional.of(mapper.toDTO(updatedFuncionario));
            } else {
                throw new IllegalArgumentException("Senha antiga incorreta");
            }
        } else {
            return Optional.empty();
        }
    }
}
