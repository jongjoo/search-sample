package com.example.searchsample.search.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class SearchPlaceServiceTest {


    @Autowired
    SearchPlaceService searchPlaceService;

    @Test
    void call() {
        String onsureProdVer = "10";
        String vupOnsureProdVer = String.valueOf(Integer.parseInt(onsureProdVer) + 1);
        System.out.println(vupOnsureProdVer);

    }

    @Test
    void searchTest() {
        String input = "카카오뱅크";
        var res = searchPlaceService.search(input);
        System.out.println(res);
    }
}