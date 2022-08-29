package com.example.searchsample.common.dto;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

/**
 * The type Response record.
 * @param httpStatus 응답 httpStatus
 * @param headers 응답 headers
 * @param payload 응답 payload
 */
public record ResponseRecord(
        HttpStatus httpStatus,
        HttpHeaders headers,
        String payload) {
}
