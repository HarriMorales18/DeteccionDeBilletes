package com.peaje.backend.infrastructure.adapter.out.persistence.repository;

import com.peaje.backend.infrastructure.adapter.out.persistence.entity.DenominacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataDenominacionRepository extends JpaRepository<DenominacionEntity, Long> {
}