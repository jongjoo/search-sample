package com.example.searchsample.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DevSiteCode {

    NAVER("0", "네이버"),
    KAKAO("1", "카카오");

    private final String codeId;
    private final String codeName;

}

