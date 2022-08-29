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
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 주소 검색에 대한 기능
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SearchPlaceService {

    private static final int RESULT_MAX_SIZE = 10;

    private final KakaoPlaceService kakaoPlaceService;
    private final NaverPlaceService naverPlaceService;
    private final SearchHistoryService searchHistoryService;

    /**
     * 검색을 위한 기능
     * 타이틀을 입력받아서 장소 검색에 대한 n개 응답을 논블럭킹 방식으로 송수신.
     *
     * @param name 검색어
     * @return SearchPlaceResponse
     */
    public SearchPlaceResponse search(String name) {
        List<Queue<PlaceDto>> queueList = new ArrayList<>();
        searchHistoryService.saveSearchWord(name);
        var fetchResponse = fetchSearch(name).collectList().block();

        if (!ObjectUtils.isEmpty(fetchResponse)) {
            for (PlaceResultDto placeDtoList : fetchResponse) {
                composeSearchApiSite(placeDtoList, queueList);
            }
        }
        var searchPlaceList = searchResult(queueList);
        return new SearchPlaceResponse(searchPlaceList);
    }

    /**
     * 응답을 구성할 사이트 셋팅.
     *
     * @param placeDtoList 장소 API Response Dto
     * @param queueList 장소 API Response dto List
     */
    private void composeSearchApiSite(PlaceResultDto placeDtoList, List<Queue<PlaceDto>> queueList) {
        if (DevSiteCode.KAKAO.equals(placeDtoList.devSiteCode())) {
            var kakaoPlaceDtoList = placeDtoList.placeDtoList();
            if (!CollectionUtils.isEmpty(placeDtoList.placeDtoList())) {
                queueList.add(new LinkedList<>(kakaoPlaceDtoList));
            }
        }

        if (DevSiteCode.NAVER.equals(placeDtoList.devSiteCode())) {
            var naverPlaceDtoList = placeDtoList.placeDtoList();
            if (!CollectionUtils.isEmpty(placeDtoList.placeDtoList())) {
                queueList.add(new LinkedList<>(naverPlaceDtoList));
            }
        }

        // ... other api
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
     * @param queueList 장소 API Response dto List
     * @return List<SearchPlace>
     */
    private List<SearchPlace> searchResult(List<Queue<PlaceDto>> queueList) {
        List<SearchPlace> resultList = similarSearchPlace(queueList);
        extraSearchPlace(resultList, queueList);
        return resultList;
    }

    /**
     * n개의 응답을 회기하여 모든 경우의 수 구성
     * 유사한 장소가 나왔을 결우 결과 dto에 구성하며 queue에서 제거.
     *
     * @param queueList 장소 API Response dto List
     * @return List<SearchPlace>
     */
    private List<SearchPlace> similarSearchPlace(List<Queue<PlaceDto>> queueList) {
        List<PlaceDto> resultList = new ArrayList<>();
        for (int i = 0; i + 1 < queueList.size(); i++) {
            similarSearchPlaceDfs(resultList, queueList, i, i + 1);
        }
        return builderSearchPlaceList(resultList);
    }

    private void similarSearchPlaceDfs(List<PlaceDto> resultList, List<Queue<PlaceDto>> queueList, int index, int target) {
        if (queueList.size() == target) {
            return;
        }
        Queue<PlaceDto> firstQueue = queueList.get(index);
        Queue<PlaceDto> secondQueue = queueList.get(target);
        for (int i = 0; i < firstQueue.size(); i++) {
            PlaceDto firstPlaceDto = firstQueue.poll();
            int size = secondQueue.size();
            for (int j = 0; j < size; j++) {
                PlaceDto secondPlaceDto = secondQueue.poll();
                if (isSamePlace(firstPlaceDto, secondPlaceDto)) {
                    resultList.add(firstPlaceDto);
                    j = 10;
                } else {
                    secondQueue.offer(secondPlaceDto);
                }
            }
            if (size == secondQueue.size()) {
                firstQueue.offer(firstPlaceDto);
            }
        }
        similarSearchPlaceDfs(resultList, queueList, index, target + 1);
    }

    /**
     * 유사하지 않은 장소를 검색 API를 설정한 순서대로 동적 구성.
     * ex) kakao, naver, google 순으로 적재.
     *
     * @param resultList 결과 dto
     * @param queueList  API 검색 결과 List<Queue<PlaceDto>>
     */
    private void extraSearchPlace(List<SearchPlace> resultList, List<Queue<PlaceDto>> queueList) {
        AtomicInteger count = new AtomicInteger(3);
        while (isResultSize(resultList, count)) {
            count.set(0);
            queueList.forEach(queue -> {
                        if (isAddResultList(resultList, queue)) {
                            var element = queue.poll();
                            SearchPlace searchPlace =
                                    new SearchPlace(element.placeName(), element.address(), element.roadAddress(), element.phone());
                            resultList.add(searchPlace);
                            count.getAndIncrement();
                        }
                    }
            );
        }
    }

    private List<SearchPlace> builderSearchPlaceList(List<PlaceDto> resultList) {
        List<SearchPlace> searchPlaceList = new ArrayList<>();
        resultList.forEach(placeDto -> {
            var searchPlace = new SearchPlace(placeDto.placeName(), placeDto.address(), placeDto.roadAddress(), placeDto.phone());
            searchPlaceList.add(searchPlace);
        });
        return searchPlaceList;
    }

    private boolean isResultSize(List<SearchPlace> resultList, AtomicInteger count) {
        return resultList.size() < RESULT_MAX_SIZE && count.intValue() > 0;
    }

    private boolean isAddResultList(List<SearchPlace> resultList, Queue<PlaceDto> queue) {
        return !queue.isEmpty() && resultList.size() < RESULT_MAX_SIZE;
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
        return second.contains(first) || first.contains(second);
    }

    private <T> int getMinSize(List<T> first, List<T> second) {
        int minSize = first.size();
        if (minSize > second.size()) {
            minSize = second.size();
        }
        return minSize;
    }
}
