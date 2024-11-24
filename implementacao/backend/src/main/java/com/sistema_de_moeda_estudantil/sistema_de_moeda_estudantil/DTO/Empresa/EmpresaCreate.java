package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Empresa;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmpresaCreate {
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O CNPJ é obrigatório")
    private String cnpj;

    @NotBlank(message = "O email é obrigatório")
    private String email;
}
