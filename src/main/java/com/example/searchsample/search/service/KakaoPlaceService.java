package com.example.searchsample.search.service;

import com.example.searchsample.common.component.WebClientComponent;
import com.example.searchsample.common.dto.RequestRecord;
import com.example.searchsample.common.dto.ResponseRecord;
import com.example.searchsample.search.config.KakaoProperties;
import com.example.searchsample.search.constant.KakaoKeys;
import com.example.searchsample.search.constant.NaverKeys;
import com.example.searchsample.search.dto.PlaceResultDto;
import com.example.searchsample.common.utils.RestUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
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
        return responseRecordMono.map(d -> {
            return new PlaceResultDto(d.payload(), d.payload());
        });
    }

    private Mono<ResponseRecord> doGet(String uri) {
        Map<String, String> headers = builderHeaders();
        RequestRecord<Object> requestRecord = builderRequestRecord(uri, headers, null);
        return webClientComponent.sendRequest(requestRecord, HttpMethod.GET);
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
