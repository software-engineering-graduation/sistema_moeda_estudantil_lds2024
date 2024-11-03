package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.security.AuthenticationService;

@RestController
@RequestMapping("/authenticate")
public class AuthenticationController {
  @Autowired
  private AuthenticationService authenticationService;

  @PostMapping
  public String authenticate(
      Authentication authentication) {
    return authenticationService.authenticate(authentication);
  }
}
