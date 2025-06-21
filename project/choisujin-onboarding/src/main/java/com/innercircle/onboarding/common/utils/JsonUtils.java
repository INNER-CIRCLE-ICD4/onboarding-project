package com.innercircle.onboarding.common.utils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innercircle.onboarding.common.exceptions.CommonException;
import com.innercircle.onboarding.common.response.ResponseStatus;

public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 1. Dto Object → JsonNode
    public static JsonNode toJsonNode(Object dto) {
        return objectMapper.valueToTree(dto);
    }

    // 2. JsonNode → Dto Object class
    public static <T> T toObject(JsonNode jsonNode, Class<T> clazz) {
        try {
            return objectMapper.treeToValue(jsonNode, clazz);
        } catch (Exception e) {
            throw new CommonException(ResponseStatus.JSON_CONVERT_FAIL, "[toObject] : " + e.getMessage());
        }
    }

    // 3. Dto Object → Json String
    public static String toJsonString(Object dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (Exception e) {
            throw new CommonException(ResponseStatus.JSON_CONVERT_FAIL, "[toJsonString] : " + e.getMessage());
        }
    }

}
