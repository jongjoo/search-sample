package com.example.searchsample.search.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoPlaceMeta(
        @JsonProperty("total_count")
        int totalCount,
        @JsonProperty("same_name")
        KakaoPlaceSameName sameName,
        @JsonProperty("pageable_count")
        int pageableCount,
        @JsonProperty("is_end")
        boolean isEnd
) {

}
