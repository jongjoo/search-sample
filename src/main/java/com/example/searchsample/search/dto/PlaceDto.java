package com.example.searchsample.search.dto;

public record PlaceDto(
        String placeName,
        String address,
        String roadAddress,
        String phone,
        String x,
        String y
) {
}
