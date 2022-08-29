package com.example.searchsample.search.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class NaverPlaceServiceTest {

    @Autowired
    NaverPlaceService naverPlaceService;

    @DisplayName("네이버 검색 테스트")
    @Test
    void search() {
        String word = "카카오뱅크";
        var response = naverPlaceService.search(word).block();

        assertTrue(response.placeDtoList().size() > 0, "네이버 검색 응답값이 없습니다.");
        System.out.println(response);
    }
}