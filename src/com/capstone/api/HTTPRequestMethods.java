package com.capstone.api;

public enum HTTPRequestMethods {

    GET("GET"),
    PUT("PUT"),
    POST("POST"),
    DELETE("DELETE"),
    PATCH("PATCH");

    private String value;

    private HTTPRequestMethods(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
