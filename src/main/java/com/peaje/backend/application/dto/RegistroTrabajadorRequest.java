package com.peaje.backend.application.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RegistroTrabajadorRequest {
    private String cedula;
    private String password;
    private String nombres;
    private String apellidos;
    private Integer edad;
    private String nacionalidad;
    private LocalDate fechaNacimiento;
    private String lugarNacimiento;
    private String correo;
    private String telefono;
    private String rol; // Opcional (por defecto será "TRABAJADOR")
}