package com.example.searchsample.search.service;

import com.example.searchsample.common.code.DevSiteCode;
import com.example.searchsample.common.component.WebClientComponent;
import com.example.searchsample.common.dto.RequestRecord;
import com.example.searchsample.common.dto.ResponseRecord;
import com.example.searchsample.common.utils.JsonUtils;
import com.example.searchsample.common.utils.RestUtils;
import com.example.searchsample.search.config.KakaoProperties;
import com.example.searchsample.search.constant.KakaoKeys;
import com.example.searchsample.search.dto.KakaoPlaceResponse;
import com.example.searchsample.search.dto.PlaceDto;
import com.example.searchsample.search.dto.PlaceResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 카카오 장소 검색 기능
 */
@Service
@RequiredArgsConstructor
public class KakaoPlaceService implements PlaceService {

    private final WebClientComponent webClientComponent;
    private final KakaoProperties kakaoProperties;

    public Mono<PlaceResultDto> search(String name) {
        String uri = builderSearchPlaceUri(name);
        Mono<ResponseRecord> responseRecordMono = doGet(uri);
        return responseRecordMono.mapNotNull(res -> {
            if(res.httpStatus().isError()){
                return null;
            }
            var kakaoPlaceResponse = JsonUtils.toObject(res.payload(), KakaoPlaceResponse.class);
            var placeDtoList = builderPlaceDtoList(kakaoPlaceResponse);
            return new PlaceResultDto(placeDtoList, DevSiteCode.KAKAO);
        });
    }

    private Mono<ResponseRecord> doGet(String uri) {
        Map<String, String> headers = builderHeaders();
        RequestRecord<Object> requestRecord = builderRequestRecord(uri, headers, null);
        return webClientComponent.sendRequest(requestRecord, HttpMethod.GET);
    }

    private List<PlaceDto> builderPlaceDtoList(KakaoPlaceResponse kakaoPlaceResponse) {
        return kakaoPlaceResponse.documents().stream()
                .map(d -> new PlaceDto(d.place_name(), d.address_name(), d.road_address_name(), d.phone(), d.x(), d.y()))
                .toList();
    }

    private String builderSearchPlaceUri(String name) {
        Map<String, String> uriMap = new HashMap<>();
        uriMap.put(KakaoKeys.SEARCH_WORD, name);
        uriMap.put(KakaoKeys.RESULT_PAGE_SIZE, "1");
        uriMap.put(KakaoKeys.RESULT_SIZE, "10");
        uriMap.put(KakaoKeys.RESULT_SORT, "accuracy");
        return RestUtils.builderParamsUri(KakaoKeys.SEARCH_PLACE_JSON_URI, uriMap);
    }

    private Map<String, String> builderHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.AUTHORIZATION, kakaoProperties.authorization());
        return headers;
    }

    private <T> RequestRecord<T> builderRequestRecord(String uri, Map<String, String> headers, T payload) {
        return new RequestRecord<>(
                kakaoProperties.host(),
                uri,
                headers,
                payload
        );
    }
}
