package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Aluno;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Data
public class AlunoCreate {
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
    private String senha;

    @NotBlank(message = "O CPF é obrigatório")
    private String cpf;

    private String rg;
    private String endereco;
    private String curso;

    private Integer instituicaoId;
}


