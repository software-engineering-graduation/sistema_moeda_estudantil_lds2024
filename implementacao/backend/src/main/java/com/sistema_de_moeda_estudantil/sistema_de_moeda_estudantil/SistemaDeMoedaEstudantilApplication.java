package com.sistema_de_moeda_estudantil.sistema_de_moeda_estudantil;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableScheduling
public class SistemaDeMoedaEstudantilApplication {

	public static void main(String[] args) {
		SpringApplication.run(SistemaDeMoedaEstudantilApplication.class, args);
	}

	@Bean
	ApplicationRunner runner(PasswordEncoder passwordEncoder) {
		return args -> System.out.println(passwordEncoder.encode("password"));
	}
}
