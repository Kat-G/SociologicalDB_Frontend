package com.example.sociologicaldb_frontend.configuration;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Service
public class TableInfoService {
    private final RestTemplate restTemplate;

    public TableInfoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, List<String>> fetchTableInfo() {
        String url = "http://localhost:8080/dbInfo";
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, List<String>>>() {}).getBody();
    }
}