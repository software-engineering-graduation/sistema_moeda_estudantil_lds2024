package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.service.EmailServiceJustForTesting;

@RestController
public class EmailControllerJustForTesting {

    @Autowired
    private EmailServiceJustForTesting emailServiceJustForTesting;

    @GetMapping("/send-email")
    public String sendEmail(
        @RequestParam String to, 
        @RequestParam String subject, 
        @RequestParam String text
    ) {
        try {
            emailServiceJustForTesting.sendEmail(to, subject, text);
            return "E-mail enviado com sucesso!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao enviar e-mail: " + e.getMessage();
        }
    }
}