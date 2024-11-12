package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Empresa.EmpresaCreate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Empresa.EmpresaDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Empresa;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.FuncionarioEmpresa;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Usuario;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.enums.TipoUsuario;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.mapper.EmpresaMapper;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.EmpresaRepository;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.FuncionarioEmpresaRepository;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.UsuarioRepository;

@Service
public class EmpresaService {

    private EmpresaRepository empresaRepository;
    private FuncionarioEmpresaRepository funcionarioEmpresaRepository;
    private UsuarioRepository usuarioRepository;
    private final EmpresaMapper mapper = EmpresaMapper.INSTANCE;

    public EmpresaService(EmpresaRepository empresaRepository,
            FuncionarioEmpresaRepository funcionarioEmpresaRepository, UsuarioRepository usuarioRepository) {
        this.empresaRepository = empresaRepository;
        this.funcionarioEmpresaRepository = funcionarioEmpresaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<EmpresaDTO> listarTodas(Authentication authentication) {
        Usuario usuario = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (usuario.getTipo() == TipoUsuario.ADMIN) {
            return empresaRepository.findAll().stream()
                    .map(mapper::toDTO)
                    .collect(Collectors.toList());
        }

        FuncionarioEmpresa funcionario = funcionarioEmpresaRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        Empresa empresa = empresaRepository.findById(funcionario.getEmpresa().getId())
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
        
        return List.of(mapper.toDTO(empresa));
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
