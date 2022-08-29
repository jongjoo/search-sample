package com.example.searchsample.search.service;

import com.example.searchsample.search.dto.PlaceResultDto;
import reactor.core.publisher.Mono;

/**
 * 장소
 */
public interface PlaceService {

    /**
     * 입력된 word 로 장소를 검색한다
     *
     * @param word the placeName
     * @return the mono
     */
    Mono<PlaceResultDto> search(String word);
}
