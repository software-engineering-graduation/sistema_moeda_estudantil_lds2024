package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Empresa;

import lombok.Data;

@Data
public class EmpresaDTO {
    private Long id;
    private String nome;
    private String cnpj;
    private Long saldoMoedas;
}
