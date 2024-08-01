package com.capstone.api;

public enum ContentTypeOptions {

    APPLICATION_JSON("application/json"),
    APPLICATION_SOAP_XML("application/soap+xml"),
    TEXT_XML("text/xml"),
    APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),
    MULTI_PART_FORM_DATA("multipart/form-data");

    private String value;

    private ContentTypeOptions(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
