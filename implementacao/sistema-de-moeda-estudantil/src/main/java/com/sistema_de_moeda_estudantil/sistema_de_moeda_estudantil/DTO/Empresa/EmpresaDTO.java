package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Empresa;

import lombok.Data;

@Data
public class EmpresaDTO {
    private int id;
    private String nome;
    private String email;
    private String cnpj;
    private int saldoMoedas;
}
