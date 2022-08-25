package com.example.searchsample.search.service;

import com.example.searchsample.common.component.WebClientComponent;
import com.example.searchsample.common.utils.RestUtils;
import com.example.searchsample.search.config.NaverProperties;
import com.example.searchsample.search.constant.NaverKeys;
import com.example.searchsample.search.dto.PlaceResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
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
        String url = naverProperties.host();
        String uri = builderSearchPlaceUri(name);

        WebClient webClient = webClientComponent.builderWebClient(url);
        return webClient.get()
                .uri(uri)
                .header(NaverKeys.NAVER_CLIENT_ID, naverProperties.clientId())
                .header(NaverKeys.NAVER_CLIENT_SECRET, naverProperties.clientSecret())
                .retrieve()
                .bodyToMono(String.class).map(body -> {
                    return new PlaceResultDto(body, body);
                });
    }

    private String builderSearchPlaceUri(String name) {
        Map<String, String> uriMap = new HashMap<>();
        uriMap.put(NaverKeys.SEARCH_WORD, name);
        uriMap.put(NaverKeys.RESULT_PAGE_SIZE, "1");
        uriMap.put(NaverKeys.RESULT_SIZE, "5");
        uriMap.put(NaverKeys.RESULT_SORT, "random");
        return RestUtils.builderParamsUri(NaverKeys.SEARCH_PLACE_JSON_URI, uriMap);
    }
}
