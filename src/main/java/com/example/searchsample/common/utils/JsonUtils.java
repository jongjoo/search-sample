package com.example.searchsample.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Json 관련 유틸s
 */
public abstract class JsonUtils {

    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);
    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
    }

    private JsonUtils() {
    }


    /**
     * json String을 Class로 매핑
     *
     * @param <T>        the type parameter
     * @param jsonString the json string
     * @param objectType the object type
     * @return the t
     */
    public static <T> T toObject(String jsonString, Class<T> objectType) {
        T object = null;
        try {
            object = mapper.readerFor(objectType).readValue(jsonString);
        } catch (JsonProcessingException e) {
            log.warn("JsonUtils.toObject(jsonString, Class<T>): Convert error (JSON -> Object).", e);
        }
        return object;
    }

    /**
     * Object를 Json String 으로 매핑
     *
     * @param object the object
     * @return the string
     */
    public static String toString(Object object) {
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
