package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Vantagem.CupomResgateDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Vantagem.VantagemCreate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Vantagem.VantagemDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Aluno;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.CupomResgate;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Empresa;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Usuario;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Vantagem;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.enums.TipoUsuario;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.mapper.CupomResgateMapper;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.mapper.VantagemMapper;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.AlunoRepository;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.CupomResgateRepository;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.EmpresaRepository;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.UsuarioRepository;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.VantagemRepository;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class VantagemService {

    @Autowired
    private VantagemRepository vantagemRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private CupomResgateRepository cupomResgateRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    private final VantagemMapper vantagemMapper = VantagemMapper.INSTANCE;
    private final CupomResgateMapper cupomResgateMapper = CupomResgateMapper.INSTANCE;

    public List<VantagemDTO> getByEmpresaId(Long empresaId) {
        return vantagemRepository.findByEmpresaId(empresaId).stream()
                .map(vantagemMapper::toDTO)
                .collect(Collectors.toList());
    }

    public VantagemDTO create(Long empresaId, VantagemCreate vantagemCreate) {
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada"));

        Vantagem vantagem = vantagemMapper.toEntity(vantagemCreate);
        vantagem.setEmpresa(empresa);

        return vantagemMapper.toDTO(vantagemRepository.save(vantagem));
    }

    public VantagemDTO update(Long empresaId, Long vantagemId, VantagemDTO vantagemUpdate) {
        Vantagem vantagem = vantagemRepository.findByIdAndEmpresaId(vantagemId, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Vantagem não encontrada"));

        vantagemMapper.updateEntityFromDTO(vantagemUpdate, vantagem);

        return vantagemMapper.toDTO(vantagemRepository.save(vantagem));
    }

    public Optional<VantagemDTO> getById(Long empresaId, Long vantagemId) {
        return vantagemRepository.findByIdAndEmpresaId(vantagemId, empresaId)
                .map(vantagemMapper::toDTO);
    }

    public void delete(Long empresaId, Long vantagemId) {
        Vantagem vantagem = vantagemRepository.findByIdAndEmpresaId(vantagemId, empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Vantagem não encontrada"));
        vantagemRepository.delete(vantagem);
    }

    public CupomResgateDTO resgatarVantagem(Long alunoId, Long vantagemId) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));
        Vantagem vantagem = vantagemRepository.findById(vantagemId)
                .orElseThrow(() -> new EntityNotFoundException("Vantagem não encontrada"));

        if (aluno.getSaldoMoedas() < vantagem.getCustoMoedas()) {
            throw new RuntimeException("Saldo insuficiente");
        }

        if (vantagem.getQuantidadeDisponivel() <= 0) {
            throw new RuntimeException("Vantagem esgotada");
        }

        aluno.setSaldoMoedas(aluno.getSaldoMoedas() - vantagem.getCustoMoedas());
        alunoRepository.save(aluno);

        vantagem.setQuantidadeDisponivel(vantagem.getQuantidadeDisponivel() - 1);
        vantagemRepository.save(vantagem);

        CupomResgate cupomResgate = new CupomResgate();
        cupomResgate.setAluno(aluno);
        cupomResgate.setVantagem(vantagem);
        cupomResgate.setEmpresa(vantagem.getEmpresa());
        cupomResgate.setCodigo(generateCodigo());
        cupomResgate.setValor(vantagem.getCustoMoedas());

        try {
            emailService.sendCupomResgateEmails(cupomResgate);
        } catch (MessagingException e) {
            throw new RuntimeException(
                    "Erro ao enviar email: Problema com o envio da mensagem. Detalhes: " + e.getMessage(), e);
        } catch (MailAuthenticationException e) {
            throw new RuntimeException(
                    "Erro ao enviar email: Falha de autenticação no servidor SMTP. Verifique suas credenciais. Detalhes: "
                            + e.getMessage(),
                    e);
        } catch (MailSendException e) {
            throw new RuntimeException(
                    "Erro ao enviar email: Falha ao conectar ou enviar o e-mail. Detalhes: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao enviar email. Detalhes: " + e.getMessage(), e);
        }

        return cupomResgateMapper.toDTO(cupomResgateRepository.save(cupomResgate));
    }

    private String generateCodigo() {
        return UUID.randomUUID().toString();
    }

    public List<VantagemDTO> getByAllAvailable() {
        return vantagemRepository.findByQuantidadeDisponivelGreaterThan(0).stream()
                .map(vantagem -> vantagemMapper.toDTO(vantagem))
                .toList();
    }

    public List<CupomResgateDTO> getCuponsByUser(Authentication authentication, Long userId) {
        Usuario usuario = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<CupomResgate> cupons;

        // Se for admin, pode ver os cupons de qualquer usuário
        if (usuario.getTipo() == TipoUsuario.ADMIN) {
            cupons = cupomResgateRepository.findAll();
        } else {
            // Se não for admin, só pode ver seus próprios cupons
            cupons = cupomResgateRepository.findByAlunoId(usuario.getId());
        }

        return cupons.stream()
                .map(cupomResgateMapper::toDTO)
                .collect(Collectors.toList());
    }
}