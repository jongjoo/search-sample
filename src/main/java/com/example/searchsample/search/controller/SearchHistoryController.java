package com.example.searchsample.search.controller;

import com.example.searchsample.search.service.SearchHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 검색 키워드 목록 API
 */
@RestController
@RequestMapping("/local/history")
@RequiredArgsConstructor
public class SearchHistoryController {


    private final SearchHistoryService searchHistoryService;

    /**
     * 검색 키워드 목록
     *
     * @return the rank
     */
    @GetMapping("/rank")
    public ResponseEntity<Object> getRank() {
        try {
            var response = searchHistoryService.readSearchRank();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
