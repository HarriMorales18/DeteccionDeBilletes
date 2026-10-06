package com.peaje.backend.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "transacciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cedulaUsuario;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false)
    private Integer montoTarifa;

    @Column(nullable = false)
    private Integer montoRecibido;

    @Column(nullable = false)
    private Integer cambioTotal;

    @Column(nullable = false)
    private String tipoCambio;

    @Column(nullable = false)
    private Boolean sincronizadoNube;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_transaccion")
    private List<TransaccionDesgloseEntity> desglose = new ArrayList<>();

    // Constructor personalizado utilizado en el adaptador de persistencia
    public TransaccionEntity(LocalDateTime fechaHora, Integer montoTarifa, Integer montoRecibido,
                             Integer cambioTotal, String tipoCambio, Boolean sincronizadoNube, String cedulaUsuario) {
        this.fechaHora = fechaHora;
        this.montoTarifa = montoTarifa;
        this.montoRecibido = montoRecibido;
        this.cambioTotal = cambioTotal;
        this.tipoCambio = tipoCambio;
        this.sincronizadoNube = sincronizadoNube;
        this.cedulaUsuario = cedulaUsuario;
    }
}