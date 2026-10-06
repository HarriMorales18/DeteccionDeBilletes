package com.peaje.backend.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "transacciones_desglose")
public class TransaccionDesgloseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_denominacion", nullable = false)
    private DenominacionEntity denominacion;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private String movimiento; // "ENTRADA" (pago cliente) o "SALIDA" (devuelta)

    public TransaccionDesgloseEntity() {}

    public TransaccionDesgloseEntity(DenominacionEntity denominacion, Integer cantidad, String movimiento) {
        this.denominacion = denominacion;
        this.cantidad = cantidad;
        this.movimiento = movimiento;
    }

    public Long getId() { return id; }
    public DenominacionEntity getDenominacion() { return denominacion; }
    public Integer getCantidad() { return cantidad; }
    public String getMovimiento() { return movimiento; }
}