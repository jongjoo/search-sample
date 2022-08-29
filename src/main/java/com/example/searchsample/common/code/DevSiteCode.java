package com.example.searchsample.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 개발자 사이트 코드
 */
@Getter
@RequiredArgsConstructor
public enum DevSiteCode {

    /**
     * Naver dev site code.
     */
    NAVER("0", "네이버"),
    /**
     * Kakao dev site code.
     */
    KAKAO("1", "카카오");

    private final String codeId;
    private final String codeName;

}

