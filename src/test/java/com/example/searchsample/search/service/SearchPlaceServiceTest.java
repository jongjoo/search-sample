package com.example.searchsample.search.service;

import com.example.searchsample.search.config.NaverProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SearchPlaceServiceTest {

    @Autowired
    SearchPlaceService searchPlaceService;

    @Test
    void call() {
    }

    @Test
    void searchTest() {
        String input = "문정역 맛집";
        searchPlaceService.search(input);
    }
}