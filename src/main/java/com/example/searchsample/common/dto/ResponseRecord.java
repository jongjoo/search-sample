package com.example.searchsample.common.dto;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

public record ResponseRecord(
        HttpStatus httpStatus,
        HttpHeaders headers,
        String payload) {
}
