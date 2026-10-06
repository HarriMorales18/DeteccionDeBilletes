package com.peaje.backend.infrastructure.adapter.out.persistence;

import com.peaje.backend.application.port.out.DenominacionRepositoryPort;
import com.peaje.backend.domain.model.Denominacion;
import com.peaje.backend.infrastructure.adapter.out.persistence.entity.DenominacionEntity;
import com.peaje.backend.infrastructure.adapter.out.persistence.repository.SpringDataDenominacionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DenominacionPersistenceAdapter implements DenominacionRepositoryPort {

    private final SpringDataDenominacionRepository springDataRepository;

    public DenominacionPersistenceAdapter(SpringDataDenominacionRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public List<Denominacion> obtenerTodas() {
        return springDataRepository.findAll().stream()
                .map(entity -> new Denominacion(entity.getId(), entity.getValor(), entity.getTipo()))
                .collect(Collectors.toList());
    }

    @Override
    public Denominacion guardar(Denominacion denominacion) {
        DenominacionEntity entity = new DenominacionEntity(
                denominacion.getId(),
                denominacion.getValor(),
                denominacion.getTipo()
        );
        DenominacionEntity saved = springDataRepository.save(entity);
        return new Denominacion(saved.getId(), saved.getValor(), saved.getTipo());
    }
}