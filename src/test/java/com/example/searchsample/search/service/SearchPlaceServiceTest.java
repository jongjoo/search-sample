package com.example.searchsample.search.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.assertTrue;


@SpringBootTest
class SearchPlaceServiceTest {


    @Autowired
    SearchPlaceService searchPlaceService;

    @DisplayName("검색 테스트")
    @Test
    void searchTest() {
        String input = "카카오뱅크";
        var res = searchPlaceService.search(input);
        assertTrue("검색값이 존재하지 않습니다.",res.searchPlaceList().size() > 0);
        System.out.println(res);
    }
}