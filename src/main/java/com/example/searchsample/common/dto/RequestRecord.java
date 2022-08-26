package com.example.searchsample.common.dto;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.Map;

public record RequestRecord<T>(
        String baseUrl,
        String uri,
        Map<String, String> headers,
        T payload) {
}
