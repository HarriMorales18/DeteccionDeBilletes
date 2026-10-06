package com.peaje.backend.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String cedula;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nombres;

    @Column(nullable = false)
    private String apellidos;

    private Integer edad;
    private String nacionalidad;
    private LocalDate fechaNacimiento;
    private String lugarNacimiento;

    @Column(unique = true)
    private String correo;

    private String telefono;

    @Column(nullable = false)
    private String rol; // "ADMIN" o "TRABAJADOR"

    @Column(nullable = false)
    private Boolean bloqueado = false;
}