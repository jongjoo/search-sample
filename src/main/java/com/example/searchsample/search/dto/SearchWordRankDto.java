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
    String word;
    Long count;
}
