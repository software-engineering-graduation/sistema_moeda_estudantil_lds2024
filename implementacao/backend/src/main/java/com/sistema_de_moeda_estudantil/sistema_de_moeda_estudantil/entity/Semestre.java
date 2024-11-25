package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity;

import java.time.LocalDateTime;

import org.springframework.cglib.core.Local;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Semestre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataInicio;

    private LocalDateTime dataFim;

    private Boolean ativo;

    @ManyToOne
    @JoinColumn(name = "instituicao_id")
    @JsonManagedReference
    private Instituicao instituicao;
}
