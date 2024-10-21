package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Empresa;

import lombok.Data;

@Data
public class EmpresaCreate {
    private String nome;
    private String email;
    private String senha;
    private String cnpj;
}
