package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.DTO.UsuarioDto;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Usuario;
import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticationService {
  private JwtService jwtService;
  private final UserRepository usuarioRepository;

  public String authenticate(Authentication authentication) {
    return jwtService.generateToken(authentication);
  }

  public UsuarioDto getCurrentUser(Authentication authentication) {
    Usuario usuario = usuarioRepository.findByEmail(authentication.getName())
        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

    return UsuarioDto.builder()
        .id(usuario.getId())
        .email(usuario.getEmail())
        .nome(usuario.getNome())
        .tipo(usuario.getTipo().toString())
        .saldoMoedas(usuario.getSaldoMoedas())
        .build();
  }
}
