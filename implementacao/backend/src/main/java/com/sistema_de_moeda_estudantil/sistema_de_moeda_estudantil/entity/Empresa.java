package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Empresa extends Usuario {

    private String cnpj;
    private int saldoMoedas;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vantagem> vantagens;
}

