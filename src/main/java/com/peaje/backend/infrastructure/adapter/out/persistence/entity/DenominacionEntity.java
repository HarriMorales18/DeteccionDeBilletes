package com.peaje.backend.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "denominaciones")
public class DenominacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer valor;

    @Column(nullable = false)
    private String tipo;

    public DenominacionEntity() {}

    public DenominacionEntity(Long id, Integer valor, String tipo) {
        this.id = id;
        this.valor = valor;
        this.tipo = tipo;
    }

    public Long getId() { return id; }
    public Integer getValor() { return valor; }
    public String getTipo() { return tipo; }
}