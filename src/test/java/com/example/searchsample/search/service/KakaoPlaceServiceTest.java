package com.example.searchsample.search.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class KakaoPlaceServiceTest {

    @Autowired
    KakaoPlaceService kakaoPlaceService;

    @Test
    void search() {
        String word = "카카오뱅크";
        var response = kakaoPlaceService.search(word).block();

        assertTrue(response.placeDtoList().size() > 0, "카카오 검색 응답값이 없습니다.");
        System.out.println(response);
    }
}