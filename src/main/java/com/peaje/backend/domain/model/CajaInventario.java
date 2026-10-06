package com.peaje.backend.domain.model;

public class CajaInventario {
    private Denominacion denominacion;
    private Integer cantidad;

    public CajaInventario(Denominacion denominacion, Integer cantidad) {
        this.denominacion = denominacion;
        this.cantidad = cantidad;
    }

    public Denominacion getDenominacion() { return denominacion; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public void descontar(int cantidadADescontar) {
        this.cantidad -= cantidadADescontar;
    }
}