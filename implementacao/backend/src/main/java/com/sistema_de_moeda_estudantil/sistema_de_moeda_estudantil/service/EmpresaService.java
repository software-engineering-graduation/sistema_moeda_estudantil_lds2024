package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.service;

import org.springframework.stereotype.Service;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Empresa.EmpresaCreate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Empresa.EmpresaDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Empresa;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.mapper.EmpresaMapper;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.EmpresaRepository;

import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

@Service
public class EmpresaService {

    private EmpresaRepository empresaRepository;
    private final EmpresaMapper mapper = EmpresaMapper.INSTANCE;

    public EmpresaService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    public List<EmpresaDTO> listarTodas() {
        return empresaRepository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<EmpresaDTO> buscarPorId(Long id) {
        return empresaRepository.findById(id)
                .map(mapper::toDTO);
    }

    public EmpresaDTO salvar(EmpresaCreate empresaCreate) {
        Empresa empresa = mapper.toEntity(empresaCreate);
        Empresa savedEmpresa = empresaRepository.save(empresa);
        return mapper.toDTO(savedEmpresa);
    }

    public Optional<EmpresaDTO> atualizar(Long id, EmpresaCreate empresaCreate) {
        Optional<Empresa> empresaExistente = empresaRepository.findById(id);

        if (empresaExistente.isPresent()) {
            Empresa empresa = empresaExistente.get();
            mapper.updateEntityFromDto(empresaCreate, empresa);
            Empresa updatedEmpresa = empresaRepository.save(empresa);
            return Optional.of(mapper.toDTO(updatedEmpresa));
        } else {
            return Optional.empty();
        }
    }

    public void deletarPorId(Long id) {
        empresaRepository.deleteById(id);
    }

}
