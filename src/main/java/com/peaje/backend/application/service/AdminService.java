package com.peaje.backend.application.service;

import com.peaje.backend.application.dto.CierreTurnoResponse;
import com.peaje.backend.application.dto.RegistroTrabajadorRequest;
import com.peaje.backend.application.dto.UsuarioResponse;
import com.peaje.backend.application.port.out.CajaInventarioRepositoryPort;
import com.peaje.backend.domain.model.CajaInventario;
import com.peaje.backend.infrastructure.adapter.out.persistence.entity.TransaccionEntity;
import com.peaje.backend.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import com.peaje.backend.infrastructure.adapter.out.persistence.repository.SpringDataTransaccionRepository;
import com.peaje.backend.infrastructure.adapter.out.persistence.repository.SpringDataUsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final SpringDataUsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final CajaInventarioRepositoryPort cajaInventarioRepositoryPort;
    private final SpringDataTransaccionRepository transaccionRepository;

    public AdminService(SpringDataUsuarioRepository usuarioRepository,
                        PasswordEncoder passwordEncoder,
                        CajaInventarioRepositoryPort cajaInventarioRepositoryPort,
                        SpringDataTransaccionRepository transaccionRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.cajaInventarioRepositoryPort = cajaInventarioRepositoryPort;
        this.transaccionRepository = transaccionRepository;
    }

    // --- GESTIÓN DE TRABAJADORES ---

    public UsuarioResponse registrarTrabajador(RegistroTrabajadorRequest request) {
        if (usuarioRepository.existsByCedula(request.getCedula())) {
            throw new IllegalArgumentException("Ya existe un usuario con la cédula proporcionada.");
        }

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setCedula(request.getCedula());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setNombres(request.getNombres());
        usuario.setApellidos(request.getApellidos());
        usuario.setEdad(request.getEdad());
        usuario.setNacionalidad(request.getNacionalidad());
        usuario.setFechaNacimiento(request.getFechaNacimiento());
        usuario.setLugarNacimiento(request.getLugarNacimiento());
        usuario.setCorreo(request.getCorreo());
        usuario.setTelefono(request.getTelefono());
        usuario.setRol(request.getRol() != null ? request.getRol() : "TRABAJADOR");
        usuario.setBloqueado(false);

        UsuarioEntity guardado = usuarioRepository.save(usuario);
        return mapearAUsuarioResponse(guardado);
    }

    public List<UsuarioResponse> listarTodosLosTrabajadores() {
        return usuarioRepository.findAll().stream()
                .map(this::mapearAUsuarioResponse)
                .collect(Collectors.toList());
    }

    public UsuarioResponse actualizarTrabajador(String cedula, RegistroTrabajadorRequest request) {
        UsuarioEntity usuario = usuarioRepository.findByCedula(cedula)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con cédula: " + cedula));

        usuario.setNombres(request.getNombres());
        usuario.setApellidos(request.getApellidos());
        usuario.setEdad(request.getEdad());
        usuario.setNacionalidad(request.getNacionalidad());
        usuario.setFechaNacimiento(request.getFechaNacimiento());
        usuario.setLugarNacimiento(request.getLugarNacimiento());
        usuario.setCorreo(request.getCorreo());
        usuario.setTelefono(request.getTelefono());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return mapearAUsuarioResponse(usuarioRepository.save(usuario));
    }

    public UsuarioResponse cambiarEstadoBloqueo(String cedula, boolean bloquear) {
        UsuarioEntity usuario = usuarioRepository.findByCedula(cedula)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con cédula: " + cedula));

        usuario.setBloqueado(bloquear);
        return mapearAUsuarioResponse(usuarioRepository.save(usuario));
    }

    public void eliminarTrabajador(String cedula) {
        UsuarioEntity usuario = usuarioRepository.findByCedula(cedula)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con cédula: " + cedula));
        usuarioRepository.delete(usuario);
    }

    // --- AUDITORÍA DE CAJA EN TIEMPO REAL ---

    public Map<String, Object> obtenerAuditoriaCaja() {
        List<CajaInventario> inventario = cajaInventarioRepositoryPort.obtenerInventarioActual();

        long totalDineroEnCaja = inventario.stream()
                .mapToLong(i -> (long) i.getDenominacion().getValor() * i.getCantidad())
                .sum();

        Map<String, Integer> desgloseActual = new LinkedHashMap<>();
        inventario.forEach(i ->
                desgloseActual.put("$" + i.getDenominacion().getValor() + " (" + i.getDenominacion().getTipo() + ")", i.getCantidad())
        );

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("totalDineroEnCaja", totalDineroEnCaja);
        respuesta.put("inventarioDetallado", desgloseActual);

        return respuesta;
    }

    // --- HISTORIAL DE TRANSACCIONES Y CIERRE DE TURNO ---

    public List<TransaccionEntity> obtenerHistorialTransacciones() {
        return transaccionRepository.findAll();
    }

    public List<TransaccionEntity> obtenerTransaccionesPorTrabajador(String cedula) {
        return transaccionRepository.findByCedulaUsuario(cedula);
    }

    public CierreTurnoResponse generarCierreTurno(String cedulaTrabajador) {
        List<TransaccionEntity> txs = transaccionRepository.findByCedulaUsuario(cedulaTrabajador);

        long totalTransacciones = txs.size();
        long totalRecaudadoTarifas = txs.stream().mapToLong(TransaccionEntity::getMontoTarifa).sum();
        long totalEfectivoIngresado = txs.stream().mapToLong(TransaccionEntity::getMontoRecibido).sum();
        long totalEfectivoDevuelto = txs.stream().mapToLong(TransaccionEntity::getCambioTotal).sum();

        return new CierreTurnoResponse(
                cedulaTrabajador,
                totalTransacciones,
                totalRecaudadoTarifas,
                totalEfectivoIngresado,
                totalEfectivoDevuelto
        );
    }

    private UsuarioResponse mapearAUsuarioResponse(UsuarioEntity entity) {
        return new UsuarioResponse(
                entity.getId(),
                entity.getCedula(),
                entity.getNombres(),
                entity.getApellidos(),
                entity.getEdad(),
                entity.getNacionalidad(),
                entity.getFechaNacimiento(),
                entity.getLugarNacimiento(),
                entity.getCorreo(),
                entity.getTelefono(),
                entity.getRol(),
                entity.getBloqueado()
        );
    }
}