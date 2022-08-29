package com.example.searchsample.search.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * The type Naver place response.
 */
public record NaverPlaceResponse(
        @JsonProperty("items")
        List<NaverPlaceItems> items,
        @JsonProperty("display")
        int display,
        @JsonProperty("start")
        int start,
        @JsonProperty("total")
        int total,
        @JsonProperty("lastBuildDate")
        String lastBuildDate
) {

}
