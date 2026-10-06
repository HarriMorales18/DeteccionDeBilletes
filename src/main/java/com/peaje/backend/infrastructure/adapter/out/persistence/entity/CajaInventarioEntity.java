package com.peaje.backend.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "caja_inventario")
public class CajaInventarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_denominacion", nullable = false)
    private DenominacionEntity denominacion;

    @Column(nullable = false)
    private Integer cantidad;

    public CajaInventarioEntity() {}

    public CajaInventarioEntity(DenominacionEntity denominacion, Integer cantidad) {
        this.denominacion = denominacion;
        this.cantidad = cantidad;
    }

    public Long getId() { return id; }
    public DenominacionEntity getDenominacion() { return denominacion; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}