package com.peaje.backend.application.port.out;

import com.peaje.backend.domain.model.Denominacion;
import java.util.List;

public interface DenominacionRepositoryPort {
    List<Denominacion> obtenerTodas();
    Denominacion guardar(Denominacion denominacion);
}