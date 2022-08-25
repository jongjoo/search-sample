package com.example.searchsample.search.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "developer.naver")
public record NaverProperties(
        String host,
        String clientId,
        String clientSecret
) {
}
