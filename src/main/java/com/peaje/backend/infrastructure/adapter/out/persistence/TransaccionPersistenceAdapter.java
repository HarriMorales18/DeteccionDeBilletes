package com.peaje.backend.infrastructure.adapter.out.persistence;

import com.peaje.backend.application.port.out.TransaccionRepositoryPort;
import com.peaje.backend.domain.model.Denominacion;
import com.peaje.backend.domain.valueobject.TipoCambio;
import com.peaje.backend.infrastructure.adapter.out.persistence.entity.DenominacionEntity;
import com.peaje.backend.infrastructure.adapter.out.persistence.entity.TransaccionDesgloseEntity;
import com.peaje.backend.infrastructure.adapter.out.persistence.entity.TransaccionEntity;
import com.peaje.backend.infrastructure.adapter.out.persistence.repository.SpringDataDenominacionRepository;
import com.peaje.backend.infrastructure.adapter.out.persistence.repository.SpringDataTransaccionRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TransaccionPersistenceAdapter implements TransaccionRepositoryPort {

    private final SpringDataTransaccionRepository transaccionRepository;
    private final SpringDataDenominacionRepository denominacionRepository;

    public TransaccionPersistenceAdapter(SpringDataTransaccionRepository transaccionRepository,
                                         SpringDataDenominacionRepository denominacionRepository) {
        this.transaccionRepository = transaccionRepository;
        this.denominacionRepository = denominacionRepository;
    }

    @Override
    public void registrarTransaccion(Integer montoTarifa, Integer montoRecibido, Integer cambioTotal,
                                     TipoCambio tipoCambio, Map<Denominacion, Integer> desgloseSalida,
                                     String cedulaUsuario) {

        TransaccionEntity tx = new TransaccionEntity(
                LocalDateTime.now(),
                montoTarifa,
                montoRecibido,
                cambioTotal,
                tipoCambio.name(),
                true,
                cedulaUsuario
        );

        List<TransaccionDesgloseEntity> desgloseList = new ArrayList<>();

        if (desgloseSalida != null) {
            desgloseSalida.forEach((denominacion, cantidad) -> {
                DenominacionEntity denEntity = denominacionRepository.findById(denominacion.getId()).orElseThrow();
                desgloseList.add(new TransaccionDesgloseEntity(denEntity, cantidad, "SALIDA"));
            });
        }

        tx.setDesglose(desgloseList);
        transaccionRepository.save(tx);
    }
}