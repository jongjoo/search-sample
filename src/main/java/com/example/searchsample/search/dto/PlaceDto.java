package com.example.searchsample.search.dto;

/**
 * The type Place dto.
 */
public record PlaceDto(
        String placeName,
        String address,
        String roadAddress,
        String phone,
        String x,
        String y
) {
}
