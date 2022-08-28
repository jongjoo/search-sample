package com.example.searchsample.search.service;

import com.example.searchsample.common.code.DevSiteCode;
import com.example.searchsample.search.dto.PlaceDto;
import com.example.searchsample.search.dto.PlaceResultDto;
import com.example.searchsample.search.dto.SearchPlace;
import com.example.searchsample.search.dto.SearchPlaceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 주소 검색에 대한 기능
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SearchPlaceService {

    private final KakaoPlaceService kakaoPlaceService;
    private final NaverPlaceService naverPlaceService;


    /**
     * 검색을 위한 기능
     * 타이틀을 입력받아서 네이버, 카카오의 응답을 논블럭킹 방식으로 송수신.
     *
     * @param name 검색어
     * @return SearchPlaceResponse
     */
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

    /**
     * 병렬로 두개의 통신을 진행.
     *
     * @param name 검색어
     * @return Flux<PlaceResultDto>
     */
    private Flux<PlaceResultDto> fetchSearch(String name) {
        return Flux.merge(kakaoPlaceService.search(name), naverPlaceService.search(name))
                .parallel()
                .runOn(Schedulers.boundedElastic())
                .sequential();
    }

    /**
     * 검색 진행
     *
     * @param kakaoPlaceDtoList 카카오 장소 dto List
     * @param naverPlaceDtoList 네이버 장소 dto List
     * @return List<SearchPlace>
     */
    private List<SearchPlace> searchResult(List<PlaceDto> kakaoPlaceDtoList, List<PlaceDto> naverPlaceDtoList) {
        List<SearchPlace> resultList = new ArrayList<>();
        Queue<PlaceDto> kakaoQueue = new LinkedList<>();
        Queue<PlaceDto> naverQueue = new LinkedList<>();
        if (!CollectionUtils.isEmpty(kakaoPlaceDtoList)) {
            kakaoQueue = new LinkedList<>(kakaoPlaceDtoList);
        }
        if (!CollectionUtils.isEmpty(naverPlaceDtoList)) {
            naverQueue = new LinkedList<>(naverPlaceDtoList);
        }
        similarSearchPlace(resultList, kakaoQueue, naverQueue);
        extraSearchPlace(resultList, kakaoQueue, naverQueue);
        return resultList;
    }

    /**
     * 카카오 장소를 기준으로 네이버 장소들을 순환.
     * 유사한 장소가 나왔을 결우 결과 dto에 구성하며 queue에서 제거.
     *
     * @param resultList 결과 dto
     * @param kakaoQueue 카카오 장소 queue
     * @param naverQueue 네이버 장소 queue
     */
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

    /**
     * 유사하지 않은 장소를 카카오, 네이버 순대로 나열
     *
     * @param resultList 결과 dto
     * @param kakaoQueue 카카오 장소 queue
     * @param naverQueue 네이버 장소 queue
     */
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

    /**
     * 장소 이름이 동일해야하며,
     * 전화번호, 구주소, 도로명 주소 중 2개 이상 유사하면 일치한 장소로 채택.
     *
     * @param kakaoPlaceDto 카카오 장소 dto
     * @param naverPlaceDto 네이버 장소 dto
     * @return boolean
     */
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

    /**
     * 장소 이름이 유사한지 확인.
     *
     * @param kakaoPlaceName 장소 이름
     * @param naverPlaceName 장소 이름
     * @return boolean
     */
    private boolean isSimilarName(String kakaoPlaceName, String naverPlaceName) {
        if (!StringUtils.hasText(kakaoPlaceName) || !StringUtils.hasText(naverPlaceName)) {
            return false;
        }
        return kakaoPlaceName.contains(naverPlaceName)
                || naverPlaceName.contains(kakaoPlaceName);
    }

    /**
     * 전화번호가 유사한지 확인.
     *
     * @param kakaoPlacePhone 전화번호
     * @param naverPlacePhone 전화번호
     * @return boolean
     */
    private boolean isSimilarPhone(String kakaoPlacePhone, String naverPlacePhone) {
        if (!StringUtils.hasText(kakaoPlacePhone) || !StringUtils.hasText(naverPlacePhone)) {
            return false;
        }
        return kakaoPlacePhone.contains(naverPlacePhone)
                || naverPlacePhone.contains(kakaoPlacePhone);
    }

    /**
     * 주소가 유사한지 확인.
     *
     * @param kakaoPlaceAddress 주소
     * @param naverPlaceAddress 주소
     * @return boolean
     */
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
