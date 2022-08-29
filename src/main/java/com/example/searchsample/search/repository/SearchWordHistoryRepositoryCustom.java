package com.example.searchsample.search.repository;

import com.example.searchsample.search.dto.SearchWordRankDto;

import java.util.List;

/**
 * 검색 기록에 관련된 기능
 */
public interface SearchWordHistoryRepositoryCustom {

    /**
     * 검색 조회 건수를 조회한다.
     *
     * @return the list
     */
    List<SearchWordRankDto> readSearchWordRank();
}
