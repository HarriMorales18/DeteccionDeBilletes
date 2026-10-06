package com.peaje.backend.domain.model;

import java.util.Map;

public class ResultadoCambio {
    private final boolean exito;
    private final Integer cambioTotal;
    private final Map<Denominacion, Integer> desglose;
    private final String mensajeError;

    public ResultadoCambio(boolean exito, Integer cambioTotal, Map<Denominacion, Integer> desglose, String mensajeError) {
        this.exito = exito;
        this.cambioTotal = cambioTotal;
        this.desglose = desglose;
        this.mensajeError = mensajeError;
    }

    public boolean isExito() { return exito; }
    public Integer getCambioTotal() { return cambioTotal; }
    public Map<Denominacion, Integer> getDesglose() { return desglose; }
    public String getMensajeError() { return mensajeError; }
}