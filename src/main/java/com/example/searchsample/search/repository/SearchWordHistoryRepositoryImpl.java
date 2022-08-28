package com.example.searchsample.search.repository;

import com.example.searchsample.search.dto.SearchWordRankDto;
import com.example.searchsample.search.entity.QSearchWordHistory;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SearchWordHistoryRepositoryImpl implements SearchWordHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QSearchWordHistory searchWordHistory = QSearchWordHistory.searchWordHistory;

    @Override
    public List<SearchWordRankDto> readSearchWordRank() {
        return queryFactory.select(Projections.fields(SearchWordRankDto.class
                        , searchWordHistory.word.as("word")
                        , searchWordHistory.count().as("count")))
                .from(searchWordHistory)
                .groupBy(searchWordHistory.word)
                .orderBy(searchWordHistory.count().desc())
                .limit(10)
                .fetch();
    }

}
