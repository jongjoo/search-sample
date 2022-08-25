package com.example.searchsample.search.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record KakaoPlaceResponse(
        @JsonProperty("meta")
        KakaoPlaceMeta meta,
        @JsonProperty("documents")
        List<KakaoPlaceDocuments> documents
) {

}
