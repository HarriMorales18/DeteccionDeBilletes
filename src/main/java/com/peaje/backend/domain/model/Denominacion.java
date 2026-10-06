package com.peaje.backend.domain.model;

public class Denominacion {
    private Long id;
    private Integer valor;
    private String tipo;

    public Denominacion(Long id, Integer valor, String tipo) {
        this.id = id;
        this.valor = valor;
        this.tipo = tipo;
    }

    // Getters
    public Long getId() { return id; }
    public Integer getValor() { return valor; }
    public String getTipo() { return tipo; }
}
