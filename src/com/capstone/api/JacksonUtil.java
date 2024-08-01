package com.capstone.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.capstone.utils.loggers.LoggerUtil;


import com.fasterxml.jackson.databind.JsonNode;


import java.io.IOException;
import java.util.Map;

public class JacksonUtil {

    private JacksonUtil() {

    }

    /**
     * Convert object to a Json String
     *
     * @param obj java.{@link Object}
     * @return Json String
     */
    public static String convertObjectToJsonString(Object obj) {
        try {
            return new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).writerFor(
                    obj.getClass()).writeValueAsString(obj);
        } catch (IOException e) {
            LoggerUtil.logERROR(e.getMessage(), e);
        }
        return "";
    }

    /**
     * Get Object for a given Json String. It could be either a Single Object, a Array of Objects or a Map
     *
     * @param response java.lang.{@link String} Json String
     * @param objectType Class type of the Object to be returned.
     *                 If the Object to be returned is a {@link Map}, objectType should first contain "Key Type"
     *                   followed by "Value Type"
     * @return Object for a given Json String
     */
    public static <T> T convertJsonStringToObject(String response, Class<?>... objectType) {
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
                , false);
        JavaType type = null;

        if (objectType.length > 1) {
            JavaType kType = objectMapper.getTypeFactory().constructType(objectType[0]);
            JavaType vType = objectMapper.getTypeFactory().constructType(objectType[1]);
            type = objectMapper.getTypeFactory().constructMapType(Map.class, kType, vType);
        } else {
            type = objectMapper.getTypeFactory().constructType(objectType[0]);
        }

        try {
            return objectMapper.readValue(response, type);
        } catch (IOException e) {
            LoggerUtil.logERROR(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Get JsonNode using a Json String
     *
     * @param response java.lang.{@link String} Json String
     * @return com.syscolabs.cx.qe.core.automation.api.JacksonUtils.getJsonNode
     */
    public static JsonNode getJsonNode(String response) {
        try {
            return new ObjectMapper().readTree(response);
        } catch (IOException e) {
            LoggerUtil.logERROR(e.getMessage(), e);
        }
        return null;
    }

    public static <T> T convertToObject(Object source, Class<T> targetClass) {
        try{
            return new ObjectMapper().convertValue(source, targetClass);
        }catch (Exception e){
            LoggerUtil.logERROR(e.getMessage(), e);
        }
        return null;
    }
}
