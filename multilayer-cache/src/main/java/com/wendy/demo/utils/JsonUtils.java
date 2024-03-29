package com.wendy.demo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @Description TODO
 * @Author wendyma
 * @Date 2024/1/31 21:55
 * @Version 1.0
 */
public class JsonUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static <T> String toJson(T t) {
        try {
            return MAPPER.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Write Object as json string error");
        }
    }

    public static <T> T json2Object(String content, Class<T> clazz) {
        try {
            return MAPPER.readValue(content, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Json parse error.");
        }
    }

    public static <T> T json2Object(String content, TypeReference<T> typeReference) {
        try {
            return MAPPER.readValue(content, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("Json parse error.");
        }
    }
}
