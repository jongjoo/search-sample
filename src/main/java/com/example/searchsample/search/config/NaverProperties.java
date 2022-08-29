package com.example.searchsample.search.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 네이버 개발자 API 관련 정보
 */
@ConfigurationProperties(prefix = "developer.naver")
public record NaverProperties(
        String host,
        String clientId,
        String clientSecret
) {
}
