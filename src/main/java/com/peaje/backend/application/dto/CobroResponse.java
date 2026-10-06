package com.peaje.backend.application.dto;

import java.util.Map;

public class CobroResponse {
    private boolean exito;
    private Integer cambioTotal;
    private Map<String, Integer> desglose;
    private String mensaje;

    public CobroResponse(boolean exito, Integer cambioTotal, Map<String, Integer> desglose, String mensaje) {
        this.exito = exito;
        this.cambioTotal = cambioTotal;
        this.desglose = desglose;
        this.mensaje = mensaje;
    }

    public boolean isExito() { return exito; }
    public Integer getCambioTotal() { return cambioTotal; }
    public Map<String, Integer> getDesglose() { return desglose; }
    public String getMensaje() { return mensaje; }
}