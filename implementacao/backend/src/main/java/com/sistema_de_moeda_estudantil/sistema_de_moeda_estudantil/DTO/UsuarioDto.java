package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioDto {
    private Long id;
    private String nome;
    private String email;
    private String tipo;
    private Double saldoMoedas;
}