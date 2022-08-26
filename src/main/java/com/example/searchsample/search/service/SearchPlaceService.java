package com.example.searchsample.search.service;

import com.example.searchsample.search.dto.PlaceResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchPlaceService {

    private final KakaoPlaceService kakaoPlaceService;
    private final NaverPlaceService naverPlaceService;


    public Object search(String name) {
        var tmp = fetchUserAndOtherUser(name).collectList();

        return tmp.block();
    }

    public Flux<PlaceResultDto> fetchUserAndOtherUser(String name) {
        return Flux.merge(kakaoPlaceService.search(name), naverPlaceService.search(name))
                .parallel()
                .runOn(Schedulers.boundedElastic())
                .sequential();
//                .ordered((u1, u2) -> u2.id() - u1.id());
    }


}
