package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    @JsonBackReference
    private Instituicao instituicao;

    @OneToMany(mappedBy = "origem")
    @JsonManagedReference
    private List<Transacao> transacoes;
}
