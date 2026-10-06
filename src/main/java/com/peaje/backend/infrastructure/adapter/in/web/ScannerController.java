package com.peaje.backend.infrastructure.adapter.in.web;

import com.peaje.backend.application.service.ScannerBilleteClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/scanner")
public class ScannerController {

    private final ScannerBilleteClient scannerClient;

    public ScannerController(ScannerBilleteClient scannerClient) {
        this.scannerClient = scannerClient;
    }

    @PostMapping("/escaneo-billete")
    public ResponseEntity<Map<String, Object>> escanearBillete(@RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(scannerClient.escanearYDetectar(file));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "exito", false,
                    "mensaje", "Error al comunicarse con el microservicio de escaneo: " + e.getMessage()
            ));
        }
    }
}