package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil.entity.Usuario;

public class UserAuthenticated implements UserDetails {
  private final Usuario user;

  public UserAuthenticated(Usuario user) {
    this.user = user;
  };

  @Override
  public String getUsername() {
    return user.getEmail();
  }

  @Override
  public String getPassword() {
    return user.getSenha();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of((GrantedAuthority) () -> user.getTipo().name());
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}

