package com.peaje.backend.domain.service;

import com.peaje.backend.domain.model.CajaInventario;
import com.peaje.backend.domain.model.Denominacion;
import com.peaje.backend.domain.model.ResultadoCambio;
import com.peaje.backend.domain.valueobject.TipoCambio;

import java.util.*;

public class CalculoCambioService {

    public ResultadoCambio calcularDevuelta(
            Integer montoTarifa,
            Integer montoRecibido,
            TipoCambio tipoCambio,
            List<CajaInventario> inventarioDisponible) {

        int cambioRequerido = montoRecibido - montoTarifa;

        if (cambioRequerido < 0) {
            return new ResultadoCambio(false, 0, Collections.emptyMap(), "El monto recibido es menor a la tarifa");
        }

        if (cambioRequerido == 0) {
            return new ResultadoCambio(true, 0, Collections.emptyMap(), "Pago exacto, no requiere devuelta");
        }

        List<CajaInventario> inventarioOrdenado = ordenarSegunEstrategia(inventarioDisponible, tipoCambio);

        Map<Denominacion, Integer> desgloseResultado = new LinkedHashMap<>();
        int restante = cambioRequerido;

        for (CajaInventario item : inventarioOrdenado) {
            int valor = item.getDenominacion().getValor();
            int disponibles = item.getCantidad();

            if (valor <= restante && disponibles > 0) {
                int necesarias = restante / valor;
                int aEntregar = Math.min(necesarias, disponibles);

                if (aEntregar > 0) {
                    desgloseResultado.put(item.getDenominacion(), aEntregar);
                    restante -= (aEntregar * valor);
                }
            }

            if (restante == 0) break;
        }

        // Si BAJO o MEDIO no pueden completar el cambio exacto por falta de stock, recurre a ALTO como respaldo
        if (restante > 0 && tipoCambio != TipoCambio.ALTO) {
            return calcularDevuelta(montoTarifa, montoRecibido, TipoCambio.ALTO, inventarioDisponible);
        }

        if (restante > 0) {
            return new ResultadoCambio(false, cambioRequerido, Collections.emptyMap(),
                    "No hay suficiente efectivo en la caja para dar el cambio exacto");
        }

        return new ResultadoCambio(true, cambioRequerido, desgloseResultado, "Cambio calculado con éxito");
    }

    private List<CajaInventario> ordenarSegunEstrategia(List<CajaInventario> inventario, TipoCambio tipo) {
        List<CajaInventario> copia = new ArrayList<>(inventario);

        switch (tipo) {
            case ALTO:
                // Prioridad absoluta a billetes grandes ($100k, $50k, $20k)
                copia.sort((a, b) -> b.getDenominacion().getValor().compareTo(a.getDenominacion().getValor()));
                break;

            case MEDIO:
                // Envía billetes >= $20.000 al final. Prioriza billetes de $10.000 y $5.000
                copia.sort((a, b) -> {
                    boolean aEsAlto = a.getDenominacion().getValor() >= 20000;
                    boolean bEsAlto = b.getDenominacion().getValor() >= 20000;
                    if (aEsAlto && !bEsAlto) return 1;
                    if (!aEsAlto && bEsAlto) return -1;
                    return b.getDenominacion().getValor().compareTo(a.getDenominacion().getValor());
                });
                break;

            case BAJO:
                // Envía billetes >= $10.000 al final. Prioriza $5.000, $2.000 y monedas
                copia.sort((a, b) -> {
                    boolean aEsAlto = a.getDenominacion().getValor() >= 10000;
                    boolean bEsAlto = b.getDenominacion().getValor() >= 10000;
                    if (aEsAlto && !bEsAlto) return 1;
                    if (!aEsAlto && bEsAlto) return -1;
                    return b.getDenominacion().getValor().compareTo(a.getDenominacion().getValor());
                });
                break;
        }

        return copia;
    }
}