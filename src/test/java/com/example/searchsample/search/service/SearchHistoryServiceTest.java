package com.example.searchsample.search.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Rollback(value = false)
class SearchHistoryServiceTest {

    @Autowired
    SearchHistoryService searchHistoryService;

    @Test
    void readSearchRank() {
        var searchWordRankDtoList = searchHistoryService.readSearchRank();
        System.out.println(searchWordRankDtoList);
    }

    @Test
    void saveSearchWord() {
        String word = "카뱅";
        searchHistoryService.saveSearchWord(word);
        searchHistoryService.saveSearchWord(word);
        searchHistoryService.saveSearchWord(word);
        searchHistoryService.saveSearchWord(word);
        searchHistoryService.saveSearchWord(word);
        searchHistoryService.saveSearchWord(word);
        word = "카카오뱅크";
        searchHistoryService.saveSearchWord(word);
        searchHistoryService.saveSearchWord(word);
        searchHistoryService.saveSearchWord(word);
        searchHistoryService.saveSearchWord(word);
        searchHistoryService.saveSearchWord(word);

        var searchWordRankDtoList = searchHistoryService.readSearchRank();
        assertTrue(searchWordRankDtoList.size()> 0,"검색 목록이 조회되지 않습니다.");
        System.out.println(searchWordRankDtoList);
    }
}