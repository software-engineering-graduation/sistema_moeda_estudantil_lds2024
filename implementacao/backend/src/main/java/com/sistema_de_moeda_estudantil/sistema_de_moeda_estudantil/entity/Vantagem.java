package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vantagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String descricao;
    private String foto;
    private int custoMoedas;
    private int quantidadeDisponivel;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
}

