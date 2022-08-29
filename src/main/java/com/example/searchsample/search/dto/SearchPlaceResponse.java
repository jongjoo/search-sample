package com.example.searchsample.search.dto;

import java.util.List;

/**
 * The type Search place response.
 * @param searchPlaceList 검색 장소 리스트
 */
public record SearchPlaceResponse(
        List<SearchPlace> searchPlaceList
) {
}
