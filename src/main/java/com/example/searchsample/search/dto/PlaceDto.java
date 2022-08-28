package com.example.searchsample.search.dto;

import com.example.searchsample.common.code.DevSiteCode;

public record PlaceDto(
        String placeName,
        String address,
        String roadAddress,
        String phone,
        String x,
        String y,
        DevSiteCode devSiteCode
) {
// kakao - naver
// place_name - title
// address_name - address 비교군.
// road_address_name - roadAddress
// phone - telephone
// x
// y
}
