package com.example.searchsample.common.dto;

import java.util.Map;

/**
 * The type Request record.
 *
 * @param <T> payload tpye
 * @param baseUrl prefix url (ex. http://localhost:8080 )
 * @param uri 기능 uri  (ex. /products
 * @param headers hashMap 구조로 headers 구성
 * @param payload payload (to Json or to Xml)
 */
public record RequestRecord<T>(
        String baseUrl,
        String uri,
        Map<String, String> headers,
        T payload) {
}
