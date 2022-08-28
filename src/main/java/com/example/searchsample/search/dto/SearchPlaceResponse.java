package com.example.searchsample.search.dto;

import java.util.List;

public record SearchPlaceResponse(
        List<SearchPlace> searchPlaceList
) {
}
