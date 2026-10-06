package com.peaje.backend.application.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class ScannerBilleteClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${python.service.url:http://localhost:8000/api/detectar-billete}")
    private String pythonServiceUrl;

    @SuppressWarnings("unchecked")
    public Map<String, Object> escanearYDetectar(MultipartFile file) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };
        body.add("file", resource);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(pythonServiceUrl, requestEntity, Map.class);
        return (Map<String, Object>) response.getBody();
    }
}