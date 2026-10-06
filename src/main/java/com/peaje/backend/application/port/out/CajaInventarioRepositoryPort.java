package com.peaje.backend.application.port.out;

import com.peaje.backend.domain.model.CajaInventario;
import java.util.List;

public interface CajaInventarioRepositoryPort {
    List<CajaInventario> obtenerInventarioActual();
    void actualizarCantidad(Long idDenominacion, Integer nuevaCantidad);
}