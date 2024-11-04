package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Aluno.AlunoCreate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Aluno.AlunoDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.transacao.TransacaoDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Aluno;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Instituicao;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Professor;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Usuario;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.enums.TipoUsuario;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.mapper.AlunoMapper;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.mapper.TransacaoMapper;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.AlunoRepository;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.InstituicaoRepository;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.ProfessorRepository;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.TransacaoRepository;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.UserRepository;

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

    @Autowired
    private TransacaoRepository transacaoRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    public List<AlunoDTO> listarTodos(Authentication authentication) {
        Usuario usuario = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if(TipoUsuario.ADMIN.equals(usuario.getTipo())) {
            return alunoRepository.findAll().stream()
                    .map(alunoMapper::toDTO)
                    .collect(Collectors.toList());
        } else {
            Professor professor = professorRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            return alunoRepository.findByInstituicaoId(professor.getInstituicao().getId()).stream()
                    .map(alunoMapper::toDTO)
                    .collect(Collectors.toList());
        }
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
        return transacaoRepository.findByOrigemIdOrDestinoId(id, id)
                .stream()
                .map(transacaoMapper::toDTO)
                .collect(Collectors.toList());
    }

}
