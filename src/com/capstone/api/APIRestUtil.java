package com.capstone.api;

import com.capstone.utils.loggers.LoggerUtil;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.params.CoreConnectionPNames;

import java.util.Map;

public class APIRestUtil {
	private static final long CONNECTION_TIMEOUT_IN_MILLIS = 30000;
    private final String apiHost;
    private final String basePath;
    private final int port;
	private static final long API_REQUEST_RETRY_COUNT = 2;

    public APIRestUtil(String host, String basePath, int port) {
        this.apiHost = host;
        this.basePath = basePath;
        this.port = port;
    }

    public String getApiHost() {
        return apiHost;
    }

    public String getBasePath() {
        return basePath;
    }

    public int getPort() {
        return port;
    }

    private void logRequestURLAndBody(String bodyString, String requestURL) {
        LoggerUtil.logINFO("\n\nREQUEST_URL\n" + requestURL + "\n********\n\n");
        if (bodyString != null && !bodyString.isEmpty()) {
            LoggerUtil.logINFO("\n\nREQUEST_BODY\n" + bodyString + "\n********\n\n");
        }
    }

    private void logResponse(Response response) {
        if (response != null) {
            LoggerUtil.logINFO("\n\nRESPONSE\n" + response.asString() + "\n*********\n\n");
        } else {
            LoggerUtil.logWARNING("\n\nThe Response was null\n*********\n\n", null);
        }
    }

    @SuppressWarnings("deprecation")
    private HttpClientConfig getConnectionTimeoutConfigs() {
        HttpClientConfig httpClientConfig = new HttpClientConfig();
        httpClientConfig.setParam(ClientPNames.CONN_MANAGER_TIMEOUT, CONNECTION_TIMEOUT_IN_MILLIS);
        httpClientConfig.setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT_IN_MILLIS);
        httpClientConfig.setParam(CoreConnectionPNames.SO_TIMEOUT, CONNECTION_TIMEOUT_IN_MILLIS);
        httpClientConfig.setParam(CoreConnectionPNames.STALE_CONNECTION_CHECK, true);
        return httpClientConfig;
    }

    /**
     * Send the specs to execute the request
     *
     * @param headers         java.{@link Map}
     * @param bodyString      java.lang.{@link String}
     * @param queryParameters java.{@link Map}
     * @param uri             java.lang.{@link String}
     * @param requestMethod   java.HTTPRequestMethods
     * @return {@link Response}
     */
    public Response send(Map<String, String> headers, String bodyString, String uri, HTTPRequestMethods requestMethod,
                         Map<String, String> queryParameters) {
        return send(headers, bodyString, uri, requestMethod, queryParameters, null);
    }

    /**
     * @param headers         Header Parameters
     * @param bodyString      Body String
     * @param uri             Resource URI
     * @param requestMethod   HTTP Request Type
     * @param queryParameters Query Parameters
     * @param cookies         Request Cookies
     * @return {@link Response}
     */
    public Response send(Map<String, String> headers, String bodyString, String uri, HTTPRequestMethods requestMethod,
                         Map<String, String> queryParameters, Map<String, String> cookies) {
        return send(headers, bodyString, uri, requestMethod, queryParameters, cookies, true);
    }

    /**
     * @param headers         Header Parameters
     * @param bodyString      Body String
     * @param uri             Resource URI
     * @param requestMethod   HTTP Request Type
     * @param queryParameters Query Parameters
     * @param cookies         Request Cookies
     * @return {@link Response}
     */
    public Response send(Map<String, String> headers, String bodyString, String uri, HTTPRequestMethods requestMethod,
                         Map<String, String> queryParameters, Map<String, String> cookies, boolean printLogs) {
        headers.put("syy-correlation-id", setCorrelationID());
        if (printLogs) {
            logRequestURLAndBody(bodyString, apiHost + "/" + basePath);
        }
        RequestSpecification requestSpecification = RestAssured.given().spec(getRequestSpec(headers, bodyString,
                queryParameters));
        if (cookies != null && !cookies.isEmpty()) {
            requestSpecification = requestSpecification.cookies(cookies);
        }

        Response response = null;
        int retryCount = 0;
        while (response == null || isRetryRequired(response.statusCode())) {
            response = execute(requestMethod, requestSpecification, uri);
            retryCount++;
            if (printLogs) {
                LoggerUtil.logINFO("API '{}' request attempt '{}' to path '{}' with body '{}'"
                        , requestMethod.value(), retryCount, uri, bodyString);
                logResponse(response);
            }
            else {
                LoggerUtil.logINFO("API '{}' request attempt '{}' to path '*****' with body '*****'"
                        , requestMethod.value(), retryCount);
            }

            // +1 to the retry count because retry 0 means the original attempt, retry 1 means it should cycle 2 times
            // (including original attempt)
            if (retryCount >= (API_REQUEST_RETRY_COUNT + 1)) {
                LoggerUtil.logINFO("Max request attempts '{}' reached. Returning response...", retryCount);
                break;
            }
        }

        return response;
    }

    /**
     * Execute the request for the given specs
     *
     * @param reqMethod   java.lang.HTTPRequestMethods
     * @param requestSpec java.RequestSpecification
     * @param uri         java.lang.{@link String}
     * @return {@link Response}
     */
    private Response execute(HTTPRequestMethods reqMethod, RequestSpecification requestSpec, String uri) {
        RequestSpecification requestSpecification = RestAssured.given(requestSpec).config(RestAssured.config().
                encoderConfig(EncoderConfig.encoderConfig().
                        appendDefaultContentCharsetToContentTypeIfUndefined(false)).
                httpClient(getConnectionTimeoutConfigs()));

        Response response = null;
        if (reqMethod.equals(HTTPRequestMethods.GET)) {
            response = requestSpecification.expect().when().get(uri);
        } else if (reqMethod.equals(HTTPRequestMethods.POST)) {
            response = requestSpecification.expect().when().post(uri);
        } else if (reqMethod.equals(HTTPRequestMethods.PUT)) {
            response = requestSpecification.expect().when().put(uri);
        } else if (reqMethod.equals(HTTPRequestMethods.DELETE)) {
            response = requestSpecification.expect().when().delete(uri);
        } else if (reqMethod.equals(HTTPRequestMethods.PATCH)) {
            response = requestSpecification.expect().when().patch(uri);
        }

        return response;
    }

    /**
     * Get the Request Specifications
     *
     * @param headers         java.{@link Map}
     * @param body            java.lang.{@link String}
     * @param queryParameters java.{@link Map}
     * @return {@link RequestSpecification}
     */
    private RequestSpecification getRequestSpec(Map<String, String> headers, String body, Map<String, ?> queryParameters) {
        RequestSpecBuilder reqSpecBuilder = new RequestSpecBuilder();

        reqSpecBuilder.setBaseUri(getApiHost());
        reqSpecBuilder.setBasePath(getBasePath());
        reqSpecBuilder.setPort(getPort());

        if (headers != null) {
            reqSpecBuilder.addHeaders(headers);
        }

        if (body != null && body.length() > 0) {
            reqSpecBuilder.setBody(body);
        }

        if (queryParameters != null && !queryParameters.isEmpty())
            reqSpecBuilder.addQueryParams(queryParameters);

        return reqSpecBuilder.build();
    }

    private boolean isRetryRequired(int responseCode) {
        return responseCode >= 400;
    }

    private String setCorrelationID() {
        return "E2E-" + System.currentTimeMillis();
    }
}
