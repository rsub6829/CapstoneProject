package com.capstone.api;

import com.capstone.utils.Properties;

public class APIConstants {

    private APIConstants() {
    }

    private static final Properties cxPropertiesMicroservices = new Properties(APIConstants.class, "api");

    // --------------> Order Service
    public static final String ORDER_SERVICE_API_HOST =
            cxPropertiesMicroservices.getEnvProperty("orderService.api.host");
    public static final String ORDER_SERVICE_API_BASE_PATH =
            cxPropertiesMicroservices.getEnvProperty("orderService.api.basepath");
    public static final int ORDER_SERVICE_API_PORT =
            Integer.parseInt(cxPropertiesMicroservices.getEnvProperty("orderService.api.port"));

    // --------------> Config Service
    public static final String CONFIG_SERVICE_API_HOST =
            cxPropertiesMicroservices.getEnvProperty("configService.api.host");

    // --------------> Permission Service
    public static final String PERMISSION_SERVICE_API_HOST =
            cxPropertiesMicroservices.getEnvProperty("permissionService.api.host");
    public static final String PERMISSION_SERVICE_API_BASE_PATH =
            cxPropertiesMicroservices.getEnvProperty("permissionService.api.basepath");
    public static final int PERMISSION_SERVICE_API_PORT =
            Integer.parseInt(cxPropertiesMicroservices.getEnvProperty("permissionService.api.port"));

    // --------------> GraphQL Service

    public static final String GRAPHQL_SERVICE_API_HOST =
            cxPropertiesMicroservices.getEnvProperty("graphQL.api.host");

    public static final String GRAPHQL_SERVICE_API_BASE_PATH =
            cxPropertiesMicroservices.getEnvProperty("graphQL.api.basepath");

    public static final int GRAPHQL_SERVICE_API_PORT =
            Integer.parseInt(cxPropertiesMicroservices.getEnvProperty("graphQL.api.port"));

    public static final String CONFIG_SERVICE_API_BASE_PATH =
            cxPropertiesMicroservices.getEnvProperty("configService.api.basepath");
    public static final int CONFIG_SERVICE_API_PORT =
            Integer.parseInt(cxPropertiesMicroservices.getEnvProperty("configService.api.port"));

    // --------------> BFF
    public static final String BFF_API_HOST = cxPropertiesMicroservices.getEnvProperty("mss.web.bff.api.host");
    public static final String BFF_API_BASE_PATH = cxPropertiesMicroservices.getEnvProperty("mss.web.bff.api.basepath");
    public static final int BFF_API_PORT =
            Integer.parseInt(cxPropertiesMicroservices.getEnvProperty("mss.web.bff.api.port"));

    // BFF - Auth
    public static final String BFF_JWT_SIGN_ISSUER = "sysco";
    public static final String BFF_JWT_SIGN_AUDIENCE = "cxs-shop";
    public static final String BFF_SESSION_COOKIE_NAME = "MSS_STATEFUL";
    public static final String BFF_MSS_ERROR_COOKIE_NAME = "MSS_SESSION_ERROR";

    // BFF - Misc
    public static final String BFF_SYSTEM_IPH_VIEW_ID = "2";

    // --------------> Delivery  Creation
    public static final String DELIVERY_API_HOST = cxPropertiesMicroservices.getEnvProperty("mss.web.delivery.route.api.host");
    public static final String DELIVERY_API_BASE_PATH = cxPropertiesMicroservices.getEnvProperty("mss.web.delivery.route.api.basepath");
    public static final int DELIVERY_API_PORT = Integer.parseInt(cxPropertiesMicroservices.getEnvProperty("mss.web.delivery.route.api.port"));

    // --------------> Punch-out API
    public static final String PUNCHOUT_API_HOST = cxPropertiesMicroservices.getEnvProperty("punchoutService.api.host");
    public static final String PUNCHOUT_API_BASE_PATH = cxPropertiesMicroservices.getEnvProperty("punchoutService.api.basepath");
    public static final int PUNCHOUT_API_PORT = Integer.parseInt(cxPropertiesMicroservices.getEnvProperty("punchoutService.api.port"));
}
