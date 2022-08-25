package com.example.searchsample.search.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "developer.kakao")
public record KakaoProperties(
        String host,
        String authorization
) {
}
