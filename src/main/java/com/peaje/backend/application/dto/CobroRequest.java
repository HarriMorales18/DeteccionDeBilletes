package com.peaje.backend.application.dto;

import com.peaje.backend.domain.valueobject.TipoCambio;

public class CobroRequest {
    private Integer montoTarifa;   // Ej: 2500
    private Integer montoRecibido; // Ej: 50000
    private TipoCambio tipoCambio; // ALTO, MEDIO o BAJO

    public CobroRequest() {}

    public CobroRequest(Integer montoTarifa, Integer montoRecibido, TipoCambio tipoCambio) {
        this.montoTarifa = montoTarifa;
        this.montoRecibido = montoRecibido;
        this.tipoCambio = tipoCambio;
    }

    public Integer getMontoTarifa() { return montoTarifa; }
    public Integer getMontoRecibido() { return montoRecibido; }
    public TipoCambio getTipoCambio() { return tipoCambio; }
}