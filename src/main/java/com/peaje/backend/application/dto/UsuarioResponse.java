package com.peaje.backend.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UsuarioResponse {
    private Long id;
    private String cedula;
    private String nombres;
    private String apellidos;
    private Integer edad;
    private String nacionalidad;
    private LocalDate fechaNacimiento;
    private String lugarNacimiento;
    private String correo;
    private String telefono;
    private String rol;
    private Boolean bloqueado;
}