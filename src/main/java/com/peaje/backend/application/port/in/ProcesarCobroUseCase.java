package com.peaje.backend.application.port.in;

import com.peaje.backend.application.dto.CobroRequest;
import com.peaje.backend.application.dto.CobroResponse;

public interface ProcesarCobroUseCase {
    CobroResponse ejecutar(CobroRequest request);
}