package com.example.searchsample.search.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 검색 키워드 목록
 */
@Getter
@Setter
@ToString
public class SearchWordRankDto {
    /**
     * 키워드
     */
    String word;
    /**
     * 검색 건수
     */
    Long count;
}
