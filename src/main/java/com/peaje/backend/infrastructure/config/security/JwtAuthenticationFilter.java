package com.peaje.backend.infrastructure.config.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                DecodedJWT decodedJWT = jwtUtil.validarToken(token);
                String cedula = decodedJWT.getSubject();
                String rol = decodedJWT.getClaim("rol").asString();

                // Registrar al usuario en el contexto de seguridad de Spring
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        cedula, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol))
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                // Token inválido o expirado
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }
}