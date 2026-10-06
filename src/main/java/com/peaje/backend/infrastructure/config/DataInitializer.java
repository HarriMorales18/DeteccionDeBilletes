package com.peaje.backend.infrastructure.config;

import com.peaje.backend.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import com.peaje.backend.infrastructure.adapter.out.persistence.repository.SpringDataUsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final SpringDataUsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(SpringDataUsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Si no existe el administrador maestro, lo crea automáticamente
        if (!usuarioRepository.existsByCedula("987654321")) {
            UsuarioEntity admin = new UsuarioEntity();
            admin.setCedula("987654321");
            admin.setPassword(passwordEncoder.encode("181818h")); // Encriptado nativo por Spring
            admin.setNombres("Administrador");
            admin.setApellidos("Principal");
            admin.setEdad(30);
            admin.setNacionalidad("Colombiana");
            admin.setFechaNacimiento(LocalDate.of(1995, 1, 1));
            admin.setLugarNacimiento("Bogotá");
            admin.setCorreo("admin3@peaje.com");
            admin.setTelefono("3001234567");
            admin.setRol("ADMIN");
            admin.setBloqueado(false);

            usuarioRepository.save(admin);
            System.out.println(">>> ADMINISTRADOR MAESTRO CREADO EXITOSAMENTE (Cédula: 123456789) <<<");
        }
    }
}