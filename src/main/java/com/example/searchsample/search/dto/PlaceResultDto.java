package com.example.searchsample.search.dto;

import java.util.List;

public record PlaceResultDto(
        List<PlaceDto> placeDtoList
) {
}
