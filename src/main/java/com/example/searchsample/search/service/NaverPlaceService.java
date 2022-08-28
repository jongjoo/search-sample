package com.example.searchsample.search.service;

import com.example.searchsample.common.code.DevSiteCode;
import com.example.searchsample.common.component.WebClientComponent;
import com.example.searchsample.common.dto.RequestRecord;
import com.example.searchsample.common.dto.ResponseRecord;
import com.example.searchsample.common.utils.JsonUtils;
import com.example.searchsample.common.utils.RestUtils;
import com.example.searchsample.search.config.NaverProperties;
import com.example.searchsample.search.constant.NaverKeys;
import com.example.searchsample.search.dto.NaverPlaceResponse;
import com.example.searchsample.search.dto.PlaceDto;
import com.example.searchsample.search.dto.PlaceResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 네이버 장소 검색 기능
 */
@Service
@RequiredArgsConstructor
public class NaverPlaceService implements PlaceService {

    private final WebClientComponent webClientComponent;
    private final NaverProperties naverProperties;

    public Mono<PlaceResultDto> search(String name) {
        String uri = builderSearchPlaceUri(name);
        Mono<ResponseRecord> responseRecordMono = doGet(uri);
        return responseRecordMono.map(res -> {
            var naverPlaceResponse = JsonUtils.toObject(res.payload(), NaverPlaceResponse.class);
            var placeDtoList = builderPlaceDtoList(naverPlaceResponse);
            return new PlaceResultDto(placeDtoList, DevSiteCode.NAVER);
        });
    }

    private Mono<ResponseRecord> doGet(String uri) {
        Map<String, String> headers = builderHeaders();
        RequestRecord<Object> requestRecord = builderRequestRecord(uri, headers, null);
        return webClientComponent.sendRequest(requestRecord, HttpMethod.GET);
    }

    private List<PlaceDto> builderPlaceDtoList(NaverPlaceResponse naverPlaceResponse) {
        return naverPlaceResponse.items().stream()
                .map(d -> new PlaceDto(formatterTagString(d.title()), d.address(), d.roadAddress(), d.telephone(), d.mapx(), d.mapy()))
                .toList();
    }

    private String formatterTagString(String input) {
        String str = input.replace("<b>", "");
        return str.replace("</b>", "");
    }

    private String builderSearchPlaceUri(String name) {
        Map<String, String> uriMap = new HashMap<>();
        uriMap.put(NaverKeys.SEARCH_WORD, name);
        uriMap.put(NaverKeys.RESULT_PAGE_SIZE, "1");
        uriMap.put(NaverKeys.RESULT_SIZE, "10");
        uriMap.put(NaverKeys.RESULT_SORT, "random");
        return RestUtils.builderParamsUri(NaverKeys.SEARCH_PLACE_JSON_URI, uriMap);
    }

    private Map<String, String> builderHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put(NaverKeys.NAVER_CLIENT_ID, naverProperties.clientId());
        headers.put(NaverKeys.NAVER_CLIENT_SECRET, naverProperties.clientSecret());
        return headers;
    }

    private <T> RequestRecord<T> builderRequestRecord(String uri, Map<String, String> headers, T payload) {
        return new RequestRecord<>(
                naverProperties.host(),
                uri,
                headers,
                payload
        );
    }
}
