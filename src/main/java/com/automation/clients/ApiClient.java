package com.automation.clients;

import com.automation.config.ConfigManager;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

/**
 * Base API Client with RestAssured configuration
 */
public class ApiClient {
    private static ApiClient instance;
    private RequestSpecification requestSpec;
    private ResponseSpecification responseSpec;
    private ConfigManager configManager;

    private ApiClient() {
        configManager = ConfigManager.getInstance();
        setupRestAssured();
        buildRequestSpecification();
        buildResponseSpecification();
    }

    public static ApiClient getInstance() {
        if (instance == null) {
            synchronized (ApiClient.class) {
                if (instance == null) {
                    instance = new ApiClient();
                }
            }
        }
        return instance;
    }

    private void setupRestAssured() {
        RestAssured.baseURI = configManager.getBaseUrl();
        RestAssured.config = RestAssuredConfig.config()
                .sslConfig(SSLConfig.sslConfig().allowAllHostnames());
    }

    private void buildRequestSpecification() {
        RequestSpecBuilder requestBuilder = new RequestSpecBuilder();
        requestBuilder.setBaseUri(configManager.getBaseUrl());
        requestBuilder.setBasePath(configManager.getBasePath());
        requestBuilder.setContentType(ContentType.JSON);
        requestBuilder.addHeader(configManager.getAuthKey(),configManager.getAuthToken());
        requestBuilder.addHeader("Accept", "application/json");
        requestBuilder.setAuth(RestAssured.basic(configManager.getAuthKey(),configManager.getAuthToken()));


        // Add filters for logging and Allure reporting
        requestBuilder.addFilter(new RequestLoggingFilter());
        requestBuilder.addFilter(new ResponseLoggingFilter());
        requestBuilder.addFilter(new AllureRestAssured());

        requestSpec = requestBuilder.build();

    }

    private void buildResponseSpecification() {
        ResponseSpecBuilder responseBuilder = new ResponseSpecBuilder();
        responseSpec = responseBuilder.build();

    }

    public RequestSpecification getRequestSpec() {
        return RestAssured.given().spec(requestSpec);
    }

    public ResponseSpecification getResponseSpec() {
        return responseSpec;
    }

    public RequestSpecification getAuthenticatedRequestSpec() {
        // Add authentication if needed
        return getRequestSpec()
                .auth()
                .basic(configManager.getProperty("auth.username"), 
                       configManager.getProperty("auth.password"));
    }
}