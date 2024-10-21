package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Aluno;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Empresa;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Instituicao;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Vantagem;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.AlunoRepository;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.EmpresaRepository;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.InstituicaoRepository;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.VantagemRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private InstituicaoRepository instituicaoRepository;
    @Autowired
    private AlunoRepository alunoRepository;
    @Autowired
    private EmpresaRepository empresaRepository;
    @Autowired
    private VantagemRepository vantagemRepository;
    

    @Override
    public void run(String... args) throws Exception {
        addInstituicoes();
        addAlunos();
        addEmpresas();
        addVantagens();
    }

    private void addInstituicoes() {
        Instituicao instituicao = new Instituicao();
        instituicao.setNome("PUC Minas");
        instituicao.setEndereco("Rua Claudio Manoel, 1162 - Funcionários, Belo Horizonte - MG, 30140-100");
        instituicaoRepository.save(instituicao);
    }

    private void addAlunos() {
        Aluno aluno = new Aluno();
        aluno.setNome("Sabrina Carpenter");
        aluno.setSenha("123456");
        aluno.setEmail("aluno.email@email.com");
        aluno.setCpf("123.456.789-00");
        aluno.setRg("12.345.678-9");
        aluno.setEndereco("Avenida Expresso, 150, Belo Horizonte - MG, 70707-070");
        aluno.setCurso("Engenharia de Software");
        aluno.setSaldoMoedas(100);
        aluno.setInstituicao(instituicaoRepository.findAll().getFirst());
        alunoRepository.save(aluno);
    }

    private void addEmpresas() {
        Empresa empresa = new Empresa();
        empresa.setCnpj("12.345.678/0001-90");
        empresaRepository.save(empresa);
    }

    private void addVantagens() {
        Empresa empresa = empresaRepository.findAll().getFirst();
        List<Vantagem> vantagens = new ArrayList<>();
        Vantagem vantagem1 = new Vantagem();
        vantagem1.setDescricao("Desconto de 10% em qualquer produto");
        vantagem1.setFoto("https://www.google.com.br");
        vantagem1.setCustoMoedas(10);
        vantagem1.setQuantidadeDisponivel(100);

        Vantagem vantagem2 = new Vantagem();
        vantagem2.setDescricao("Desconto de 20% em qualquer produto");
        vantagem2.setFoto("https://www.google.com.br");
        vantagem2.setCustoMoedas(20);
        vantagem2.setQuantidadeDisponivel(50);

        vantagens.add(vantagem1);
        vantagens.add(vantagem2);

        empresa.setVantagens(vantagens);
        empresaRepository.save(empresa);

        vantagemRepository.save(vantagem1);
        vantagemRepository.save(vantagem2);
    }
}

