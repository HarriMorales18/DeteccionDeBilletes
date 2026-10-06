package com.peaje.backend.infrastructure.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    // En producción, esta clave debe venir de variables de entorno (application.properties)
    private static final String SECRET_KEY = "PeajeSecretKeySuperSeguraParaProduccion";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);

    public String generarToken(String cedula, String rol) {
        return JWT.create()
                .withSubject(cedula)
                .withClaim("rol", rol)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 8)) // 8 horas (un turno)
                .sign(ALGORITHM);
    }

    public DecodedJWT validarToken(String token) {
        return JWT.require(ALGORITHM)
                .build()
                .verify(token);
    }
}