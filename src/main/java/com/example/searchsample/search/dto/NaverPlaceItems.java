package com.example.searchsample.search.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NaverPlaceItems(
        @JsonProperty("mapy")
        String mapy,
        @JsonProperty("mapx")
        String mapx,
        @JsonProperty("roadAddress")
        String roadAddress,
        @JsonProperty("address")
        String address,
        @JsonProperty("telephone")
        String telephone,
        @JsonProperty("description")
        String description,
        @JsonProperty("category")
        String category,
        @JsonProperty("link")
        String link,
        @JsonProperty("title")
        String title
) {

}
