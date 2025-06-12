package com.automation.clients;

import com.automation.config.ConfigManager;
import com.automation.constants.EndPoints;
import com.automation.utils.LogManager;
import io.cucumber.datatable.DataTable;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class UserServiceController {
    static ApiClient apiClient = ApiClient.getInstance();

    public static String generateEmailAndPassword(int length) {
        String candidateChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder stringGenerated = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            stringGenerated.append(candidateChars.charAt(random.nextInt(candidateChars
                    .length())));
        }
        return stringGenerated.toString();
    }

    public static Response signUp(String email, String password) {
        return given().contentType(ContentType.JSON)
                .body("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}")
                .log().all()
                .when().post(EndPoints.SING_UP)
                .then().log().all().extract().response();
    }

    public static Response login(String email, String password) {
        RequestSpecification request = given().contentType(ContentType.JSON).log().all();
        if (email != null && password != null) {
            request.body("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}");
        }
        return request.when().post(EndPoints.LOG_IN)
                .then().log().all().extract().response();
    }

    public static Response getUser(String endpoint, int statusCode) {
        return apiClient.getRequestSpec()
                .basePath(ConfigManager.getInstance().getBasePath())
                .when()
                .get(endpoint)
                .then()
                .statusCode(statusCode)
                .extract().response();

    }

    public static Response getUser(String endpoint) {
        LogManager.info("GET method Executed ");
        return apiClient.getRequestSpec()
                .basePath(ConfigManager.getInstance().getBasePath())
                .when()
                .get(endpoint)
                .then()
                .extract().response();

    }

    public static Response deleteUser(String endpoint) {
        LogManager.info("Trying to execute DELETE command ...");
        Response res = apiClient.getRequestSpec()
                .when()
                .delete(endpoint)
                .then()
                .extract().response();
        LogManager.info("DELETE command Executed Successfully!");
        return res;

    }

    public static Response post(String payload, String endpoint) {
        Map<String, String> data = new HashMap<>();
        RequestSpecification request = given().contentType(ContentType.JSON).log().all();
        return  apiClient.getRequestSpec()
                .when()
                .body(payload)
                .post(endpoint)
                .then()
                .log()
                .all()
                .extract()
                .response();

    }

    public static Response createUser(String method, DataTable dataTable) {
        List<Map<String, String>> dataList = dataTable.asMaps(String.class,String.class);
        Map<String,String>data=dataList.get(0);
//        Map<String, String> data = dataTable.asMap(String.class, String.class);
        LogManager.info("Trying to create user with "+"\nData :"+ dataTable+"\nendpoint"+ EndPoints.CREATE_USER);
        String name = data.get("name");
        String job = data.get("job");
        String payload = String.format("{\"name\":\"%s\", \"job\":\"%s\"}", name, job);
        if (method.equalsIgnoreCase("POST")) {
            return apiClient.getRequestSpec()
                    .body(payload)
                    .when()
                    .post(EndPoints.CREATE_USER);
        } else if (method.equalsIgnoreCase("PUT")) {
            return apiClient.getRequestSpec()
                    .body(payload)
                    .when()
                    .put(EndPoints.UPDATE_USER);
        }


        return null;
    }

    public static Response post( String payload) {
        LogManager.info("Trying to send post request "+"\nData :"+ payload+"\nendpoint"+ EndPoints.CREATE_USER);
            return apiClient.getRequestSpec()
                    .body(payload)
                    .when()
                    .post(EndPoints.CREATE_USER);
    }


}
