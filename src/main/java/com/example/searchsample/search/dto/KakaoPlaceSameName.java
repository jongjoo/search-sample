package com.example.searchsample.search.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record KakaoPlaceSameName(
        @JsonProperty("selected_region")
        String selectedRegion,
        @JsonProperty("region")
        List<String> region,
        @JsonProperty("keyword")
        String keyword
) {

}
