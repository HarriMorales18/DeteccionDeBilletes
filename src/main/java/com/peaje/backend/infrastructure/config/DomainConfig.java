package com.peaje.backend.infrastructure.config;

import com.peaje.backend.domain.service.CalculoCambioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

    @Bean
    public CalculoCambioService calculoCambioService() {
        return new CalculoCambioService();
    }
}