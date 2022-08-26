package com.example.searchsample.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Json 관련 유틸s
 */
public class JsonUtils {

    protected JsonUtils() {
    }

    /**
     * Convert object type to Json String
     *
     * @param object the object
     * @return the string
     */
    public static String convertJsonObjectToString(Object object) {
        if (object instanceof String) {
            return String.valueOf(object);
        }
        String body = "";
        final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        try {
            body = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return body;
    }
}
