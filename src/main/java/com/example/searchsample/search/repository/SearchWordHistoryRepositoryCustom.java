package com.example.searchsample.search.repository;

import com.example.searchsample.search.dto.SearchWordRankDto;

import java.util.List;

public interface SearchWordHistoryRepositoryCustom {

    List<SearchWordRankDto> readSearchWordRank();
}
