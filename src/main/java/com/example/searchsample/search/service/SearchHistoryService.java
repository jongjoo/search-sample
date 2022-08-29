package com.example.searchsample.search.service;

import com.example.searchsample.search.dto.SearchWordRankDto;
import com.example.searchsample.search.entity.SearchWordHistory;
import com.example.searchsample.search.repository.SearchWordHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 동시성 처리를 위한 저장을 분리해서 db에 로우단위로 저장.
 * 조회는 Redis를 사용해서 분단위 검색 목록을 노출한다.
 * 조회와 저장 테이블을 분리하고 싶으나, 스케줄러가 필요하여, Redis Cache를 활용하여 부하절감.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class SearchHistoryService {

    private final SearchWordHistoryRepository searchWordHistoryRepository;

    /**
     * 검색 키워드 목록 조회
     *
     * @return the list
     */
    @Cacheable(key = "new String(#root.targetClass.name.concat(':').concat(#root.method.name))"
            , value = "place", cacheManager = "placeCacheManager")
    public List<SearchWordRankDto> readSearchRank() {
        return searchWordHistoryRepository.readSearchWordRank();
    }

    /**
     * 검색 이력 저장
     *
     * @param word the word
     * @return the search word history
     */
    public SearchWordHistory saveSearchWord(String word) {
        SearchWordHistory searchWordHistory = new SearchWordHistory(word);
        return searchWordHistoryRepository.save(searchWordHistory);
    }


}
