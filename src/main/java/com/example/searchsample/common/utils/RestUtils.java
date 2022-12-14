package com.example.searchsample.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Rest 통신에 관련된 Utils
 */
public class RestUtils {

    /**
     * Rest 통신에 관련된 Utils
     */
    protected RestUtils() {

    }

    /**
     * Object를 MultiValueMap 으로 전환
     *
     * @param object the object
     * @return the multi value map
     */
    public static MultiValueMap<String, String> convertObjectToMultiValueMap(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> map = objectMapper.convertValue(object, new TypeReference<>() {
        });
        params.setAll(map);
        return params;
    }

    /**
     * Get queryParams 구성을 위한 매퍼
     *
     * @param url the url
     * @param uri the uri
     * @return the string
     */
    public static String builderParamsUri(String url, Object uri) {
        MultiValueMap<String, String> multiValueMap = convertObjectToMultiValueMap(uri);
        var encodeUri = UriComponentsBuilder.fromUriString(url)
                .queryParams(multiValueMap)
                .build().encode()
                .toUriString();
        return URLDecoder.decode(encodeUri, StandardCharsets.UTF_8);
    }

}
