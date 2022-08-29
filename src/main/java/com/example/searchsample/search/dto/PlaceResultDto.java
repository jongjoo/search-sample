package com.example.searchsample.search.dto;

import com.example.searchsample.common.code.DevSiteCode;

import java.util.List;

/**
 * The type Place result dto.
 */
public record PlaceResultDto(
        List<PlaceDto> placeDtoList,
        DevSiteCode devSiteCode
) {
}
