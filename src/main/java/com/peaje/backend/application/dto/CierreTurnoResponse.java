package com.peaje.backend.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CierreTurnoResponse {
    private String cedulaTrabajador;
    private Long totalTransacciones;
    private Long totalRecaudadoTarifas; // Dinero real producido (ganancia del peaje)
    private Long totalEfectivoIngresado; // Suma total entregada por los conductores
    private Long totalEfectivoDevuelto;   // Suma total devuelta como cambio
}