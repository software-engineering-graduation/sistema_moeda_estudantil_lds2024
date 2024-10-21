package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Aluno extends Usuario {

    private String cpf;
    private String rg;
    private String endereco;
    private String curso;
    private int saldoMoedas;

    @ManyToOne
    @JoinColumn(name = "instituicao_id")
    private Instituicao instituicao;
}

