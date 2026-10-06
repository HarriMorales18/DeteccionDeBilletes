package com.peaje.backend.infrastructure.adapter.in.web;

import com.peaje.backend.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import com.peaje.backend.infrastructure.adapter.out.persistence.repository.SpringDataUsuarioRepository; // Debes crear esta interfaz
import com.peaje.backend.infrastructure.config.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final SpringDataUsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(SpringDataUsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        String cedula = credenciales.get("cedula");
        String password = credenciales.get("password");

        UsuarioEntity usuario = usuarioRepository.findByCedula(cedula).orElse(null);

        // Validar que el usuario exista, la contraseña coincida (encriptada) y no esté bloqueado
        if (usuario == null || !passwordEncoder.matches(password, usuario.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Credenciales inválidas"));
        }
        if (usuario.getBloqueado()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Usuario bloqueado por el administrador"));
        }

        // Generar JWT
        String token = jwtUtil.generarToken(usuario.getCedula(), usuario.getRol());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "rol", usuario.getRol(),
                "nombres", usuario.getNombres()
        ));
    }
}