package com.peaje.backend.infrastructure.adapter.out.persistence.repository;

import com.peaje.backend.infrastructure.adapter.out.persistence.entity.TransaccionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataTransaccionRepository extends JpaRepository<TransaccionEntity, Long> {

    // Método necesario para consultar las transacciones realizadas por un operador específico
    List<TransaccionEntity> findByCedulaUsuario(String cedulaUsuario);
}