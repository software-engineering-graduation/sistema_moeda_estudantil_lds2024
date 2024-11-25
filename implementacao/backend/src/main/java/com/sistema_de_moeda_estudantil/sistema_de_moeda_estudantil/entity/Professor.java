package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Professor extends Usuario {

    private String cpf;
    private String departamento;

    @ManyToOne
    @JoinColumn(name = "instituicao_id")
    @JsonManagedReference
    private Instituicao instituicao;
}
