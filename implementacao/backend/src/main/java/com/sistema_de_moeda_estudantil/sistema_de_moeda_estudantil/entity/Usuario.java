package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.enums.TipoUsuario;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String senha;
    private Double saldoMoedas;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipo;
}
