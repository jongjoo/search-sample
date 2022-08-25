package com.example.searchsample.search.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoPlaceDocuments(
        @JsonProperty("y")
        String y,
        @JsonProperty("x")
        String x,
        @JsonProperty("road_address_name")
        String road_address_name,
        @JsonProperty("place_url")
        String place_url,
        @JsonProperty("place_name")
        String place_name,
        @JsonProperty("phone")
        String phone,
        @JsonProperty("id")
        String id,
        @JsonProperty("distance")
        String distance,
        @JsonProperty("category_name")
        String category_name,
        @JsonProperty("category_group_name")
        String category_group_name,
        @JsonProperty("category_group_code")
        String category_group_code,
        @JsonProperty("address_name")
        String address_name
) {

}
