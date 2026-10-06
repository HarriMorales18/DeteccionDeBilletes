package com.peaje.backend.infrastructure.adapter.out.persistence.repository;

import com.peaje.backend.infrastructure.adapter.out.persistence.entity.CajaInventarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataCajaInventarioRepository extends JpaRepository<CajaInventarioEntity, Long> {
    Optional<CajaInventarioEntity> findByDenominacionId(Long idDenominacion);
}