package com.peaje.backend.infrastructure.adapter.out.persistence.repository;

import com.peaje.backend.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataUsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByCedula(String cedula);
    boolean existsByCedula(String cedula);
}