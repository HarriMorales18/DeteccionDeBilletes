package com.peaje.backend.application.port.out;

import com.peaje.backend.domain.model.Denominacion;
import com.peaje.backend.domain.valueobject.TipoCambio;

import java.util.Map;

public interface TransaccionRepositoryPort {
    void registrarTransaccion(Integer montoTarifa, Integer montoRecibido, Integer cambioTotal,
                              TipoCambio tipoCambio, Map<Denominacion, Integer> desgloseSalida,
                              String cedulaUsuario);
}