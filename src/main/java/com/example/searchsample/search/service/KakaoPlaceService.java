package com.example.searchsample.search.service;

import com.example.searchsample.common.component.WebClientComponent;
import com.example.searchsample.search.config.KakaoProperties;
import com.example.searchsample.search.constant.KakaoKeys;
import com.example.searchsample.search.dto.PlaceResultDto;
import com.example.searchsample.common.utils.RestUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
        String url = kakaoProperties.host();
        String uri = builderSearchPlaceUri(name);

        WebClient webClient = webClientComponent.builderWebClient(url);
        return webClient.get()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, kakaoProperties.authorization())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .retrieve()
                .bodyToMono(String.class).map(body -> {
                    return new PlaceResultDto(body, body);
                });
    }

    private String builderSearchPlaceUri(String name) {
        Map<String, String> uriMap = new HashMap<>();
        uriMap.put(KakaoKeys.SEARCH_WORD, name);
        uriMap.put(KakaoKeys.RESULT_PAGE_SIZE, "1");
        uriMap.put(KakaoKeys.RESULT_SIZE, "10");
        uriMap.put(KakaoKeys.RESULT_SORT, "accuracy");
        return RestUtils.builderParamsUri(KakaoKeys.SEARCH_PLACE_JSON_URI, uriMap);
    }
}
