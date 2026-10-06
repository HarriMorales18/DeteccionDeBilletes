package com.peaje.backend.infrastructure.adapter.out.persistence;

import com.peaje.backend.application.port.out.CajaInventarioRepositoryPort;
import com.peaje.backend.domain.model.CajaInventario;
import com.peaje.backend.domain.model.Denominacion;
import com.peaje.backend.infrastructure.adapter.out.persistence.entity.CajaInventarioEntity;
import com.peaje.backend.infrastructure.adapter.out.persistence.repository.SpringDataCajaInventarioRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CajaInventarioPersistenceAdapter implements CajaInventarioRepositoryPort {

    private final SpringDataCajaInventarioRepository repository;

    public CajaInventarioPersistenceAdapter(SpringDataCajaInventarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CajaInventario> obtenerInventarioActual() {
        return repository.findAll().stream()
                .map(entity -> new CajaInventario(
                        new Denominacion(entity.getDenominacion().getId(), entity.getDenominacion().getValor(), entity.getDenominacion().getTipo()),
                        entity.getCantidad()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void actualizarCantidad(Long idDenominacion, Integer nuevaCantidad) {
        repository.findByDenominacionId(idDenominacion).ifPresent(entity -> {
            entity.setCantidad(nuevaCantidad);
            repository.save(entity);
        });
    }
}