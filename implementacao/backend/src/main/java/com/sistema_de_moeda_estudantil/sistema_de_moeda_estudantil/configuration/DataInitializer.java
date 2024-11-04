package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.configuration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Aluno;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Empresa;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.FuncionarioEmpresa;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Instituicao;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Usuario;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Professor;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Transacao;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Vantagem;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.enums.TipoUsuario;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.AlunoRepository;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.EmpresaRepository;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.FuncionarioEmpresaRepository;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.InstituicaoRepository;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.UserRepository;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.VantagemRepository;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.ProfessorRepository;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.TransacaoRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private InstituicaoRepository instituicaoRepository;
    private AlunoRepository alunoRepository;
    private EmpresaRepository empresaRepository;
    private VantagemRepository vantagemRepository;
    private UserRepository userRepository;
    private FuncionarioEmpresaRepository funcionarioEmpresaRepository;
    private ProfessorRepository professorRepository;
    private TransacaoRepository transacaoRepository;

    @Override
    public void run(String... args) throws Exception {
        createAdmin();
        addInstituicoes();
        addProfessores();
        addAlunos();
        addEmpresas();
        addFuncionarios();
        addVantagens();
        addTransacoes();
    }
    private void createAdmin() {
        Usuario admin = new Usuario();

        admin.setEmail("admin");
        admin.setSenha("$2a$15$RWQ4HVbd3VaJRQUoBRnn4.PonJYVy6shhXmufg/n3JxnIaD83dmDy");
        admin.setNome("Administrador");
        admin.setTipo(TipoUsuario.ADMIN);
        userRepository.save(admin);
    }

    private void addInstituicoes() {
        Instituicao instituicao = new Instituicao();
        instituicao.setNome("PUC Minas");
        instituicao.setEndereco("Rua Claudio Manoel, 1162 - Funcionários, Belo Horizonte - MG, 30140-100");
        instituicaoRepository.save(instituicao);

        Instituicao instituicao2 = new Instituicao();
        instituicao2.setNome("UFMG");
        instituicao2.setEndereco("Avenida Antônio Carlos, 6627 - Pampulha, Belo Horizonte - MG, 31270-901");
        instituicaoRepository.save(instituicao2);
    }

    private void addAlunos() {
        List<Instituicao> instituicoes = instituicaoRepository.findAll();
        Aluno aluno = new Aluno();
        aluno.setNome("Sabrina Carpenter");
        aluno.setSenha("$2a$15$b3p7lCvC6sn/EgEakqKtoOUxONvePNn.nvxoqIP8zkAE9tPZQ8IOu");
        aluno.setEmail("aluno.email@email.com");
        aluno.setCpf("123.456.789-00");
        aluno.setRg("12.345.678-9");
        aluno.setEndereco("Avenida Expresso, 150, Belo Horizonte - MG, 70707-070");
        aluno.setCurso("Engenharia de Software");
        aluno.setSaldoMoedas(100.0);
        aluno.setInstituicao(instituicoes.get(0)); // First institution
        aluno.setTipo(TipoUsuario.ALUNO);
        alunoRepository.save(aluno);

        Aluno aluno2 = new Aluno();
        aluno2.setNome("Dua Lipa");
        aluno2.setSenha("$2a$15$b3p7lCvC6sn/EgEakqKtoOUxONvePNn.nvxoqIP8zkAE9tPZQ8IOu");
        aluno2.setEmail("aluno1.email@email.com");
        aluno2.setCpf("987.654.321-00");
        aluno2.setRg("98.765.432-1");
        aluno2.setEndereco("Avenida Central, 200, São Paulo - SP, 80808-080");
        aluno2.setCurso("Engenharia de Software");
        aluno2.setSaldoMoedas(100.0);
        aluno2.setInstituicao(instituicoes.get(0)); // First institution
        aluno2.setTipo(TipoUsuario.ALUNO);
        alunoRepository.save(aluno2);

        // Adding two more students for the second institution
        Aluno aluno3 = new Aluno();
        aluno3.setNome("Olivia Rodrigo");
        aluno3.setSenha("abcdef");
        aluno3.setEmail("aluno2.email@email.com");
        aluno3.setCpf("321.654.987-00");
        aluno3.setRg("32.165.498-7");
        aluno3.setEndereco("Rua Verde, 300, Rio de Janeiro - RJ, 90909-090");
        aluno3.setCurso("Ciência da Computação");
        aluno3.setSaldoMoedas(80.0);
        aluno3.setInstituicao(instituicoes.get(1)); // Second institution
        aluno3.setTipo(TipoUsuario.ALUNO);
        alunoRepository.save(aluno3);

        Aluno aluno4 = new Aluno();
        aluno4.setNome("Billie Eilish");
        aluno4.setSenha("7891011");
        aluno4.setEmail("aluno3.email@email.com");
        aluno4.setCpf("654.321.987-00");
        aluno4.setRg("65.432.198-7");
        aluno4.setEndereco("Rua Azul, 400, Curitiba - PR, 60606-060");
        aluno4.setCurso("Sistemas de Informação");
        aluno4.setSaldoMoedas(5.0);
        aluno4.setInstituicao(instituicoes.get(1)); // Second institution
        aluno4.setTipo(TipoUsuario.ALUNO);
        alunoRepository.save(aluno4);
    }

    private void addEmpresas() {
        Empresa empresa = new Empresa();
        empresa.setNome("Gostosuras PUB");
        empresa.setCnpj("12.345.678/0001-90");
        empresaRepository.save(empresa);
    }

    private void addFuncionarios() {
        FuncionarioEmpresa funcionario1 = new FuncionarioEmpresa();
        funcionario1.setNome("João da Silva");
        funcionario1.setSenha("senha123");
        funcionario1.setEmail("joao.silva@empresa.com");
        funcionario1.setEmpresa(empresaRepository.findAll().get(0)); // First company
        funcionarioEmpresaRepository.save(funcionario1);

        FuncionarioEmpresa funcionario2 = new FuncionarioEmpresa();
        funcionario2.setNome("Maria Oliveira");
        funcionario2.setSenha("senha456");
        funcionario2.setEmail("maria.oliveira@empresa.com");
        funcionario2.setEmpresa(empresaRepository.findAll().get(0)); // First company
        funcionarioEmpresaRepository.save(funcionario2);

        // Adding two more employees for the second company
        FuncionarioEmpresa funcionario3 = new FuncionarioEmpresa();
        funcionario3.setNome("Carlos Pereira");
        funcionario3.setSenha("senha789");
        funcionario3.setEmail("carlos.pereira@empresa2.com");
        funcionario3.setEmpresa(empresaRepository.findAll().get(0)); // Second company
        funcionarioEmpresaRepository.save(funcionario3);

        FuncionarioEmpresa funcionario4 = new FuncionarioEmpresa();
        funcionario4.setNome("Ana Souza");
        funcionario4.setSenha("senha101");
        funcionario4.setEmail("ana.souza@empresa2.com");
        funcionario4.setEmpresa(empresaRepository.findAll().get(0)); // Second company
        funcionarioEmpresaRepository.save(funcionario4);
    }

    private void addVantagens() {
        Empresa empresa = empresaRepository.findAll().getFirst();
        List<Vantagem> vantagens = new ArrayList<>();
        Vantagem vantagem1 = new Vantagem();
        vantagem1.setDescricao("Desconto de 10% em qualquer produto");
        vantagem1.setFoto("https://thumbs.dreamstime.com/b/disconto-de-88000109.jpg");
        vantagem1.setCustoMoedas(10.0);
        vantagem1.setQuantidadeDisponivel(100.0);
        vantagem1.setEmpresa(empresa);

        Vantagem vantagem2 = new Vantagem();
        vantagem2.setDescricao("Desconto de 20% em qualquer produto");
        vantagem2.setFoto("https://cdn.shopify.com/s/files/1/0817/7988/4088/articles/4XOfcVjU6L9Z0yxkgW0WeI_c472c5fd-a7d2-4075-ba11-944a3f40ceb4.jpg?v=1719388916&originalWidth=1848&originalHeight=782");
        vantagem2.setCustoMoedas(20.0);
        vantagem2.setQuantidadeDisponivel(50.0);
        vantagem2.setEmpresa(empresa);

        vantagens.add(vantagem1);
        vantagens.add(vantagem2);

        vantagemRepository.save(vantagem1);
        vantagemRepository.save(vantagem2);
    }

    private void addProfessores() {
        List<Instituicao> instituicoes = instituicaoRepository.findAll();

        Professor prof1 = new Professor();
        prof1.setNome("Taylor Swift");
        prof1.setSenha("$2a$15$b3p7lCvC6sn/EgEakqKtoOUxONvePNn.nvxoqIP8zkAE9tPZQ8IOu");
        prof1.setEmail("taylor.swift@puc.edu.br");
        prof1.setCpf("111.222.333-44");
        prof1.setDepartamento("Computação");
        prof1.setSaldoMoedas(1000.0);
        prof1.setTipo(TipoUsuario.PROFESSOR);
        prof1.setInstituicao(instituicoes.get(0)); // First institution
        professorRepository.save(prof1);

        Professor prof2 = new Professor();
        prof2.setNome("Ariana Grande");
        prof2.setSenha("$2a$15$b3p7lCvC6sn/EgEakqKtoOUxONvePNn.nvxoqIP8zkAE9tPZQ8IOu");
        prof2.setEmail("ariana.grande@puc.edu.br");
        prof2.setCpf("555.666.777-88");
        prof2.setDepartamento("Computação");
        prof2.setSaldoMoedas(1000.0);
        prof2.setTipo(TipoUsuario.PROFESSOR);
        prof2.setInstituicao(instituicoes.get(0)); // First institution
        professorRepository.save(prof2);
    }

    private void addTransacoes() {
        List<Professor> professores = professorRepository.findAll();
        List<Aluno> alunos = alunoRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        // Para cada professor
        for (Professor professor : professores) {
            // 5 transações para diferentes alunos
            for (int i = 0; i < 5; i++) {
                Aluno aluno = alunos.get(i % alunos.size());
                
                Transacao transacao = new Transacao();
                transacao.setOrigem(professor);
                transacao.setDestino(aluno);
                transacao.setValor(50.0 + (i * 10)); // Valores diferentes para cada transação
                transacao.setMensagem("Ótimo trabalho na atividade " + (i + 1));
                transacao.setData(now.minusDays(i)); // Transações em dias diferentes
                
                transacaoRepository.save(transacao);

                // Atualiza saldos
                professor.setSaldoMoedas(professor.getSaldoMoedas() - transacao.getValor());
                aluno.setSaldoMoedas(aluno.getSaldoMoedas() + transacao.getValor());
                
                professorRepository.save(professor);
                alunoRepository.save(aluno);
            }
        }
    }
}
