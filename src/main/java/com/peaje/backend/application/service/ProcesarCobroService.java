package com.peaje.backend.application.service;

import com.peaje.backend.application.dto.CobroRequest;
import com.peaje.backend.application.dto.CobroResponse;
import com.peaje.backend.application.port.in.ProcesarCobroUseCase;
import com.peaje.backend.application.port.out.CajaInventarioRepositoryPort;
import com.peaje.backend.application.port.out.TransaccionRepositoryPort;
import com.peaje.backend.domain.model.CajaInventario;
import com.peaje.backend.domain.model.ResultadoCambio;
import com.peaje.backend.domain.service.CalculoCambioService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ProcesarCobroService implements ProcesarCobroUseCase {

    private final CajaInventarioRepositoryPort inventarioRepositoryPort;
    private final TransaccionRepositoryPort transaccionRepositoryPort;
    private final CalculoCambioService calculoCambioService;

    public ProcesarCobroService(CajaInventarioRepositoryPort inventarioRepositoryPort,
                                TransaccionRepositoryPort transaccionRepositoryPort,
                                CalculoCambioService calculoCambioService) {
        this.inventarioRepositoryPort = inventarioRepositoryPort;
        this.transaccionRepositoryPort = transaccionRepositoryPort;
        this.calculoCambioService = calculoCambioService;
    }

    @Override
    @Transactional
    public CobroResponse ejecutar(CobroRequest request) {
        // Obtener la cédula del trabajador autenticado
        String cedulaTrabajador = SecurityContextHolder.getContext().getAuthentication().getName();

        List<CajaInventario> inventarioReal = inventarioRepositoryPort.obtenerInventarioActual();

        // 1. EVALUAR PAGO EXACTO
        if (request.getMontoRecibido().equals(request.getMontoTarifa())) {
            registrarIngresoCaja(inventarioReal, request.getMontoRecibido());

            transaccionRepositoryPort.registrarTransaccion(
                    request.getMontoTarifa(), request.getMontoRecibido(),
                    0, request.getTipoCambio(), Collections.emptyMap(),
                    cedulaTrabajador
            );

            return new CobroResponse(true, 0, Collections.emptyMap(), "Pago exacto registrado con éxito.");
        }

        // 2. VALIDAR PAGO INSUFICIENTE
        if (request.getMontoRecibido() < request.getMontoTarifa()) {
            return new CobroResponse(false, 0, Collections.emptyMap(), "El monto recibido es insuficiente.");
        }

        // 3. CÁLCULO DE CAMBIO
        ResultadoCambio resultado = calculoCambioService.calcularDevuelta(
                request.getMontoTarifa(),
                request.getMontoRecibido(),
                request.getTipoCambio(),
                inventarioReal
        );

        if (!resultado.isExito()) {
            return new CobroResponse(false, 0, Collections.emptyMap(), resultado.getMensajeError());
        }

        // 4. Actualizar inventario de caja
        registrarIngresoCaja(inventarioReal, request.getMontoRecibido());

        resultado.getDesglose().forEach((denominacion, cantidadUsada) -> {
            inventarioReal.stream()
                    .filter(inv -> inv.getDenominacion().getId().equals(denominacion.getId()))
                    .findFirst()
                    .ifPresent(inv -> {
                        int nuevaCant = inv.getCantidad() - cantidadUsada;
                        inventarioRepositoryPort.actualizarCantidad(denominacion.getId(), nuevaCant);
                    });
        });

        // 5. Registrar la transacción asociada al operador
        transaccionRepositoryPort.registrarTransaccion(
                request.getMontoTarifa(),
                request.getMontoRecibido(),
                resultado.getCambioTotal(),
                request.getTipoCambio(),
                resultado.getDesglose(),
                cedulaTrabajador
        );

        Map<String, Integer> desgloseFormateado = new LinkedHashMap<>();
        resultado.getDesglose().forEach((denominacion, cantidad) ->
                desgloseFormateado.put("$" + denominacion.getValor() + " (" + denominacion.getTipo() + ")", cantidad)
        );

        return new CobroResponse(true, resultado.getCambioTotal(), desgloseFormateado, "Transacción completada y registrada en BD");
    }

    private void registrarIngresoCaja(List<CajaInventario> inventarioReal, Integer montoRecibido) {
        inventarioReal.stream()
                .filter(inv -> inv.getDenominacion().getValor().equals(montoRecibido))
                .findFirst()
                .ifPresent(inv -> {
                    int nuevaCant = inv.getCantidad() + 1;
                    inventarioRepositoryPort.actualizarCantidad(inv.getDenominacion().getId(), nuevaCant);
                });
    }
}