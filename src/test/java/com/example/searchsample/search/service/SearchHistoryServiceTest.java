package com.example.searchsample.search.service;

import org.junit.jupiter.api.DisplayName;
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

    @DisplayName("검색 기록 조회")
    @Test
    void readSearchRank() {
        var searchWordRankDtoList = searchHistoryService.readSearchRank();
        assertNotNull(searchWordRankDtoList);
        System.out.println(searchWordRankDtoList);
    }

    @DisplayName("검색 이력 저장")
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
        var tmp = searchHistoryService.saveSearchWord(word);

        var searchWordRankDtoList = searchHistoryService.readSearchRank();
        assertTrue(tmp.getId() > 0, "저장이 되지 않았습니다.");
        System.out.println(searchWordRankDtoList);
    }
}