package com.example.searchsample.search.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * The type Kakao place response.
 */
public record KakaoPlaceResponse(
        @JsonProperty("meta")
        KakaoPlaceMeta meta,
        @JsonProperty("documents")
        List<KakaoPlaceDocuments> documents
) {

}
