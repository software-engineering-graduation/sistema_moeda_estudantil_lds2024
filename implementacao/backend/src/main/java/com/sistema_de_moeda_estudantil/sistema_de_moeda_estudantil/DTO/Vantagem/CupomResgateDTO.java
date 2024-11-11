package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Vantagem;

import java.time.LocalDateTime;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.Empresa.EmpresaDTO;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Aluno;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CupomResgateDTO {
    private Long id;
    private String codigo;
    private Aluno aluno;
    private VantagemDTO vantagem;
    private EmpresaDTO empresa;
    private Double valor;
    private LocalDateTime data;
}
