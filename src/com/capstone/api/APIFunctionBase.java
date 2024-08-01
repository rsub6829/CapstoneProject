package com.capstone.api;


import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import com.capstone.utils.loggers.LoggerUtil;

public class APIFunctionBase {


    protected static APIRestUtil mssBffRestUtils = new APIRestUtil(APIConstants.BFF_API_HOST
            , APIConstants.BFF_API_BASE_PATH, APIConstants.BFF_API_PORT);

    

    public static Response sendAPIRequest(Map<String, String> headers, String bodyString, String uri
            , HTTPRequestMethods requestMethod, Map<String, String> queryParams, String[] cookies) {
        LoggerUtil.logINFO("REQUEST -> Send [" + requestMethod.value() + "] API Request for: "
                + mssBffRestUtils.getApiHost() + (mssBffRestUtils.getPort() > 0 ? (":"
                + mssBffRestUtils.getPort() + mssBffRestUtils.getBasePath() + uri)
                : (mssBffRestUtils.getBasePath() + uri)));
        return mssBffRestUtils.send(headers, bodyString, uri, requestMethod, queryParams, getCookieMap(cookies));
    }


    public static <T> Object getObject(String response, Class<T> c) {
        return JacksonUtil.convertJsonStringToObject(response, c);
    }

    public static Map<String, String> getCookieMap(String... cookie) {
        Map<String, String> cookies = new HashMap<>();
        if (cookie != null && cookie.length > 0) {
            cookies.put(APIConstants.BFF_SESSION_COOKIE_NAME, cookie[0]);
            if (cookie.length > 1) {
                cookies.put(APIConstants.BFF_MSS_ERROR_COOKIE_NAME, cookie[1]);
            }
        }
        return cookies;
    }

   

}
