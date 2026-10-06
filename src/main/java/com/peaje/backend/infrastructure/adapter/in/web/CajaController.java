package com.peaje.backend.infrastructure.adapter.in.web;

import com.peaje.backend.application.dto.CobroRequest;
import com.peaje.backend.application.dto.CobroResponse;
import com.peaje.backend.application.port.in.ProcesarCobroUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/caja")
@CrossOrigin(origins = "*") // Permite peticiones desde la App de Escritorio (Electron)
public class CajaController {

    private final ProcesarCobroUseCase procesarCobroUseCase;

    public CajaController(ProcesarCobroUseCase procesarCobroUseCase) {
        this.procesarCobroUseCase = procesarCobroUseCase;
    }

    @PostMapping("/procesar-cobro")
    public ResponseEntity<CobroResponse> procesarCobro(@RequestBody CobroRequest request) {
        CobroResponse response = procesarCobroUseCase.ejecutar(request);
        return ResponseEntity.ok(response);
    }
}