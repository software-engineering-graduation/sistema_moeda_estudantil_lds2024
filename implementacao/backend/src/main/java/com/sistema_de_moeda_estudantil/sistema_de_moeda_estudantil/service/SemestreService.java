package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Semestre;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.SemestreRepository;

@Service
public class SemestreService {
    private final SemestreRepository semestreRepository;

    public SemestreService(SemestreRepository semestreRepository) {
        this.semestreRepository = semestreRepository;
    }

    public Semestre novo() {
        Semestre semestre = new Semestre();
        semestre.setAtivo(true);
        semestre.setDataInicio(LocalDateTime.now());
        semestre.setDataFim(LocalDateTime.now().plusMonths(6));
        return semestreRepository.save(semestre);
    }
}
