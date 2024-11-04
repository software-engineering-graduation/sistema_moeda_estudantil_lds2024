package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.service;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Aluno.AlunoCreate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Aluno.AlunoDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.transacao.TransacaoDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Aluno;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Instituicao;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.enums.TipoUsuario;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.mapper.AlunoMapper;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.mapper.TransacaoMapper;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.AlunoRepository;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.InstituicaoRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private InstituicaoRepository instituicaoRepository;

    @Autowired
    private AlunoMapper alunoMapper;

    @Autowired
    private TransacaoMapper transacaoMapper;

    public List<AlunoDTO> listarTodos() {
        return alunoRepository.findAll().stream()
                .map(alunoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<AlunoDTO> buscarPorId(Long id) {
        return alunoRepository.findById(id)
                .map(alunoMapper::toDTO);
    }

    public AlunoDTO salvar(AlunoCreate alunoCreate) {
        Aluno aluno = alunoMapper.toEntity(alunoCreate);
        aluno.setTipo(TipoUsuario.ALUNO);

        if (alunoCreate.getInstituicaoId() != null) {
            Optional<Instituicao> instituicao = instituicaoRepository.findById(alunoCreate.getInstituicaoId());
            instituicao.ifPresent(aluno::setInstituicao);
        }

        Aluno savedAluno = alunoRepository.save(aluno);
        return alunoMapper.toDTO(savedAluno);
    }

    public Optional<AlunoDTO> atualizar(Long id, AlunoDTO alunoDto) {
        Optional<Aluno> alunoExistente = alunoRepository.findById(id);

        if (alunoExistente.isPresent()) {
            Aluno aluno = alunoExistente.get();
            alunoMapper.updateEntityFromDto(alunoDto, aluno);

            if (alunoDto.getInstituicao() != null) {
                Optional<Instituicao> instituicao = instituicaoRepository.findById(alunoDto.getInstituicao().getId());
                instituicao.ifPresent(aluno::setInstituicao);
            }

            Aluno updatedAluno = alunoRepository.save(aluno);
            return Optional.of(alunoMapper.toDTO(updatedAluno));
        } else {
            return Optional.empty();
        }
    }

    public void deletarPorId(Long id) {
        alunoRepository.deleteById(id);
    }

    public Optional<AlunoDTO> atualizarSenha(Long id, String senhaAntiga, String novaSenha) {
        Optional<Aluno> alunoExistente = alunoRepository.findById(id);

        if (alunoExistente.isPresent()) {
            Aluno aluno = alunoExistente.get();

            if (aluno.getSenha().equals(senhaAntiga)) {
                aluno.setSenha(novaSenha);
                Aluno updatedAluno = alunoRepository.save(aluno);
                return Optional.of(alunoMapper.toDTO(updatedAluno));
            } else {
                throw new IllegalArgumentException("Senha antiga incorreta");
            }
        } else {
            return Optional.empty();
        }
    }

    public List<TransacaoDTO> listarTransacoes(Long id) {
        return alunoRepository.findById(id)
                .map(Aluno::getTransacoes)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado"))
                .stream()
                .map(transacaoMapper::toDTO)
                .collect(Collectors.toList());
    }

}
