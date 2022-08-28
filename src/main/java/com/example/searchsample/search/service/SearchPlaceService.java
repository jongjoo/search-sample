package com.example.searchsample.search.service;

import com.example.searchsample.common.code.DevSiteCode;
import com.example.searchsample.search.dto.PlaceDto;
import com.example.searchsample.search.dto.PlaceResultDto;
import com.example.searchsample.search.dto.SearchPlace;
import com.example.searchsample.search.dto.SearchPlaceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchPlaceService {

    private final KakaoPlaceService kakaoPlaceService;
    private final NaverPlaceService naverPlaceService;


    public SearchPlaceResponse search(String name) {
        var fetchResponse = fetchSearch(name).collectList().block();
        List<PlaceDto> kakaoPlaceDtoList = null;
        List<PlaceDto> naverPlaceDtoList = null;
        if (!ObjectUtils.isEmpty(fetchResponse)) {
            for (PlaceResultDto placeDtoList : fetchResponse) {
                if (DevSiteCode.KAKAO.equals(placeDtoList.devSiteCode())) {
                    kakaoPlaceDtoList = placeDtoList.placeDtoList();
                }
                if (DevSiteCode.NAVER.equals(placeDtoList.devSiteCode())) {
                    naverPlaceDtoList = placeDtoList.placeDtoList();
                }
            }
        }
        var searchPlaceList = searchResult(kakaoPlaceDtoList, naverPlaceDtoList);
        return new SearchPlaceResponse(searchPlaceList);
    }

    private List<SearchPlace> searchResult(List<PlaceDto> kakaoPlaceDtoList, List<PlaceDto> naverPlaceDtoList) {
        Queue<PlaceDto> kakaoQueue = new LinkedList<>(kakaoPlaceDtoList);
        Queue<PlaceDto> naverQueue = new LinkedList<>(naverPlaceDtoList);
        List<SearchPlace> resultList = new ArrayList<>();

        similarSearchPlace(resultList, kakaoQueue, naverQueue);
        extraSearchPlace(resultList, kakaoQueue, naverQueue);
        return resultList;
    }

    private void similarSearchPlace(List<SearchPlace> resultList, Queue<PlaceDto> kakaoQueue, Queue<PlaceDto> naverQueue) {
        for (int i = 0; i < kakaoQueue.size(); i++) {
            PlaceDto kakaoElement = kakaoQueue.poll();
            int naverPlaceSize = naverQueue.size();
            for (int j = 0; j < naverPlaceSize; j++) {
                PlaceDto naverElement = naverQueue.poll();
                if (isSamePlace(kakaoElement, naverElement)) {
                    SearchPlace searchPlace =
                            new SearchPlace(kakaoElement.placeName(), kakaoElement.address(), kakaoElement.roadAddress(), kakaoElement.phone());
                    resultList.add(searchPlace);
                } else {
                    naverQueue.offer(naverElement);
                }
            }
            if (naverPlaceSize == naverQueue.size()) {
                kakaoQueue.offer(kakaoElement);
            }
        }
    }

    private void extraSearchPlace(List<SearchPlace> resultList, Queue<PlaceDto> kakaoQueue, Queue<PlaceDto> naverQueue) {
        while (resultList.size() < 10 && (!kakaoQueue.isEmpty() || !naverQueue.isEmpty())) {
            if (!kakaoQueue.isEmpty()) {
                var kakaoElement = kakaoQueue.poll();
                SearchPlace searchPlace =
                        new SearchPlace(kakaoElement.placeName(), kakaoElement.address(), kakaoElement.roadAddress(), kakaoElement.phone());
                resultList.add(searchPlace);
            }
            if (resultList.size() >= 10) {
                break;
            }
            if (!naverQueue.isEmpty()) {
                var naverElement = naverQueue.poll();
                SearchPlace searchPlace =
                        new SearchPlace(naverElement.placeName(), naverElement.address(), naverElement.roadAddress(), naverElement.phone());
                resultList.add(searchPlace);
            }
        }
    }

    private boolean isSamePlace(PlaceDto kakaoPlaceDto, PlaceDto naverPlaceDto) {
        int count = 0;
        boolean isSimilar = isSimilarName(kakaoPlaceDto.placeName(), naverPlaceDto.placeName());

        if (isSimilar) {
            if (isSimilarPhone(kakaoPlaceDto.phone(), naverPlaceDto.phone())) {
                count++;
            }
            if (isSimilarAddress(kakaoPlaceDto.address(), naverPlaceDto.address())) {
                count++;
            }
            if (isSimilarAddress(kakaoPlaceDto.roadAddress(), naverPlaceDto.roadAddress())) {
                count++;
            }
        }
        return count > 1;
    }

    public Flux<PlaceResultDto> fetchSearch(String name) {
        return Flux.merge(kakaoPlaceService.search(name), naverPlaceService.search(name))
                .parallel()
                .runOn(Schedulers.boundedElastic())
                .sequential();
    }

    private boolean isSimilarName(String kakaoPlaceName, String naverPlaceName) {
        if (!StringUtils.hasText(kakaoPlaceName) || !StringUtils.hasText(naverPlaceName)) {
            return false;
        }
        return kakaoPlaceName.contains(naverPlaceName)
                || naverPlaceName.contains(kakaoPlaceName);
    }

    private boolean isSimilarPhone(String kakaoPlacePhone, String naverPlacePhone) {
        if (!StringUtils.hasText(kakaoPlacePhone) || !StringUtils.hasText(naverPlacePhone)) {
            return false;
        }
        return kakaoPlacePhone.contains(naverPlacePhone)
                || naverPlacePhone.contains(kakaoPlacePhone);
    }

    private boolean isSimilarAddress(String kakaoPlaceAddress, String naverPlaceAddress) {
        if (kakaoPlaceAddress == null || naverPlaceAddress == null) {
            return false;
        }
        List<String> kakaoAddress = List.of(kakaoPlaceAddress.split(" "));
        List<String> naverAddress = List.of(naverPlaceAddress.split(" "));
        int minSize = getMinSize(kakaoAddress, naverAddress);
        for (int i = 0; i < minSize; i++) {
            if (!isSimilar(kakaoAddress.get(i), naverAddress.get(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean isSimilar(String first, String second) {
        return second.contains(first);
    }

    private <T> int getMinSize(List<T> first, List<T> second) {
        int minSize = first.size();
        if (minSize > second.size()) {
            minSize = second.size();
        }
        return minSize;
    }
}
