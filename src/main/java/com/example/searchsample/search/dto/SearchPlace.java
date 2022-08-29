package com.example.searchsample.search.dto;

/**
 * The type Search place.
 * @param placeName 이름
 * @param address 주소
 * @param roadAddress 도로명 주소
 * @param phone 전화번호
 */
public record SearchPlace(
        String placeName,
        String address,
        String roadAddress,
        String phone
) {
}
