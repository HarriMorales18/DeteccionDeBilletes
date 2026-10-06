package com.peaje.backend.infrastructure.adapter.in.web;

import com.peaje.backend.application.dto.CierreTurnoResponse;
import com.peaje.backend.application.dto.RegistroTrabajadorRequest;
import com.peaje.backend.application.dto.UsuarioResponse;
import com.peaje.backend.application.service.AdminService;
import com.peaje.backend.infrastructure.adapter.out.persistence.entity.TransaccionEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // 1. Registrar nuevo trabajador
    @PostMapping("/trabajadores")
    public ResponseEntity<UsuarioResponse> registrarTrabajador(@RequestBody RegistroTrabajadorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.registrarTrabajador(request));
    }

    // 2. Listar todos los trabajadores
    @GetMapping("/trabajadores")
    public ResponseEntity<List<UsuarioResponse>> listarTrabajadores() {
        return ResponseEntity.ok(adminService.listarTodosLosTrabajadores());
    }

    // 3. Editar datos de un trabajador
    @PutMapping("/trabajadores/{cedula}")
    public ResponseEntity<UsuarioResponse> actualizarTrabajador(@PathVariable String cedula,
                                                                @RequestBody RegistroTrabajadorRequest request) {
        return ResponseEntity.ok(adminService.actualizarTrabajador(cedula, request));
    }

    // 4. Bloquear o desbloquear acceso
    @PatchMapping("/trabajadores/{cedula}/bloqueo")
    public ResponseEntity<UsuarioResponse> cambiarEstadoBloqueo(@PathVariable String cedula,
                                                                @RequestParam boolean bloquear) {
        return ResponseEntity.ok(adminService.cambiarEstadoBloqueo(cedula, bloquear));
    }

    // 5. Eliminar un trabajador
    @DeleteMapping("/trabajadores/{cedula}")
    public ResponseEntity<Map<String, String>> eliminarTrabajador(@PathVariable String cedula) {
        adminService.eliminarTrabajador(cedula);
        return ResponseEntity.ok(Map.of("mensaje", "Trabajador eliminado correctamente."));
    }

    // 6. Auditoría y estado de dinero en caja
    @GetMapping("/caja/resumen")
    public ResponseEntity<Map<String, Object>> obtenerResumenCaja() {
        return ResponseEntity.ok(adminService.obtenerAuditoriaCaja());
    }

    // 7. Obtener todo el historial de transacciones
    @GetMapping("/transacciones")
    public ResponseEntity<List<TransaccionEntity>> obtenerHistorialTransacciones() {
        return ResponseEntity.ok(adminService.obtenerHistorialTransacciones());
    }

    // 8. Obtener transacciones realizadas por un operador específico
    @GetMapping("/transacciones/trabajador/{cedula}")
    public ResponseEntity<List<TransaccionEntity>> obtenerTransaccionesPorTrabajador(@PathVariable String cedula) {
        return ResponseEntity.ok(adminService.obtenerTransaccionesPorTrabajador(cedula));
    }

    // 9. Cierre de turno y arqueo de caja de un trabajador
    @GetMapping("/caja/cierre-turno/{cedula}")
    public ResponseEntity<CierreTurnoResponse> obtenerCierreTurno(@PathVariable String cedula) {
        return ResponseEntity.ok(adminService.generarCierreTurno(cedula));
    }
}