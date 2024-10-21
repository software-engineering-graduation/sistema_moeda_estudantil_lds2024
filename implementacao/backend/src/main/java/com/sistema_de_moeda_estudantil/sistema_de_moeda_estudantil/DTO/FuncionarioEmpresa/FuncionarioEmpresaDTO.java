package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.FuncionarioEmpresa;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Empresa.EmpresaDTO;

import lombok.Data;

@Data
public class FuncionarioEmpresaDTO {
    private Integer id;
    private String nome;
    private String email;
    private String senha;
}
