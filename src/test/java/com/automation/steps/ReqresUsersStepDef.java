package com.automation.steps;

import com.automation.clients.ApiClient;
import com.automation.clients.UserServiceController;
import com.automation.constants.EndPoints;
import com.automation.models.Post;
import com.automation.utils.LogManager;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import jdk.jpackage.internal.Log;
import org.testng.Assert;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.*;

public class ReqresUsersStepDef {

    private ApiClient apiClient;
    private Post testPost;
    private Response response;
    private Integer storedPostId;
    private long responseStartTime;
    @When("I send GET request to {string}")
    public void iSendGETRequestTo(String endpoint) {
        LogManager.info("Trying to get the users list....");
        response = UserServiceController.getUser(EndPoints.USER_LIST);
        /*JsonPath json = response.jsonPath();
        Assert.assertEquals(json.getString("data[3].name"),"aqua sky","Verified");*/


    }

    @When("I send GET request to retrieve all users")
    public void iSendGETRequestToRetrieveAllUsers() {
        LogManager.info("Trying to get the users list....");
        response = UserServiceController.getUser(EndPoints.USER_LIST);
    }


    @Given("I set the base URL to {string}")
    public void iSetTheBaseURLTo(String arg0) {
        LogManager.info("Initializing API client");
        apiClient = ApiClient.getInstance();
        Assert.assertNotNull(apiClient, "API client should be initialized");
    }


    @And("the response should contain minimum of {int} fields")
    public void theResponseShouldContainField(int expectedTotal) {
        LogManager.info("verify response fields should be "+expectedTotal);
        int actualTotal = response.jsonPath().getInt("total");
        Assert.assertTrue(actualTotal>=expectedTotal);
    }

    @When("I send GET request to retrieve user details")
    public void iSendGETRequestToRetrieveUserDetails() {
        LogManager.info("Trying to get the users list....");
        response = UserServiceController.getUser(EndPoints.SINGLE_USER);
    }

    @Then("I should receive a response with status code {int}")
    public void iShouldReceiveAResponseWithStatusCode(int expectedStatusCode) {

        LogManager.info("Validating response status code: " + expectedStatusCode);
        Assert.assertNotNull(response, "Response should not be null");

        int actualStatusCode = response.getStatusCode();
        Assert.assertEquals(actualStatusCode, expectedStatusCode,
                "Unexpected status code return");
    }


    @And("the response should contain a non-empty list of users")
    public void theResponseShouldContainANonEmptyListOfUsers() {
        response.then()
                .body("data", not(empty()));
    }


    @And("the response should contain user with id {int}")
    public void theResponseShouldContainUserWithId(int expectedUserId) {
        LogManager.info("verify response should contain user Id : "+expectedUserId);
        int actualTotal = response.jsonPath().getInt("data.id");
        Assert.assertEquals(actualTotal,expectedUserId);
        LogManager.info("Verified successfully!");

    }

    @And("the response should contain field {string} and value {string}")
    public void theResponseShouldContainFieldAndValue(String expectedField, String expectedValue) {
        LogManager.info("verify response should contain field : "+expectedField);
        String actualTotal = response.jsonPath().getString("data."+expectedField);
        Assert.assertEquals(actualTotal,expectedValue);
        LogManager.info("Verified successfully!");

    }


    @And("the response should contain field {string} and value should  not be NULL")
    public void theResponseShouldContainFieldAndValueShouldNotNULL(String expectedValue) {
        LogManager.info("Trying to check the value should not be NULL");
        int id=response.jsonPath().getInt(expectedValue);
        Assert.assertNotNull(id);
        LogManager.info("Successfully Verified!: Value is not null");
    }




    @And("POST or PUT response should contain field {string} and value {string} in response body")
    public void postOrPUTResponseShouldContainFieldAndValueInResponseBody(String expectedField, String expectedValue) {
        LogManager.info("verify response should contain field : "+expectedField);
        String actualTotal = response.jsonPath().getString(expectedField);
        Assert.assertEquals(actualTotal,expectedValue);
        LogManager.info("Verified successfully!");
    }


    @When("I send a DELETE request to remove the user")
    public void iSendADELETERequestToRemoveTheUser() {
        response=UserServiceController.deleteUser(EndPoints.DELETE_USER);
    }


    @When("I send GET request to {string} endpoint")
    public void iSendGETRequestToEndpoint(String endpoint) {
        response=UserServiceController.getUser(endpoint);
    }

    @And("And I store the created post data for validation")
    public void andIStoreTheCreatedPostDataForValidation() {
        LogManager.info("Trying to store id to validate...");
        testPost=new Post();
        testPost.setId( response.jsonPath().getInt("id"));
        LogManager.info("Stored Successfully!");
    }

    @And("Validat the created user id from stored data")
    public void validatTheCreatedUserIdFromStoredData() {
        LogManager.info("Trying to Validate stored data..");
        Assert.assertNotNull(testPost.getId());
        LogManager.info("Validated Successfully!");
    }

    @When("I send a {word} request to CREATE user with the following data:")
    public void iSendAPOSTRequestToCREATEUserWithTheFollowingData(String method, DataTable dataTable) {
        response=UserServiceController.createUser(method,dataTable);
    }

    @And("the response header {string} should be {string}")
    public void theResponseHeaderShouldBe(String header, String expectedHeader) {
        LogManager.info("Verifying Header "+header+"Should have "+expectedHeader);
        String actualHead=response.header(header);
        Assert.assertEquals(actualHead,expectedHeader);
        LogManager.info("Header Verified Successfully!");


    }

    @And("the response header {string} should contain {string}")
    public void theResponseHeaderShouldContain(String header, String expectedHeader) {
        LogManager.info("Verifying Header "+header+"Should have "+expectedHeader);
        String actualHead=response.header(header);
        String actualServerName = response.headers().get(header).getValue();
        Assert.assertEquals(actualServerName,expectedHeader);
        LogManager.info("Header Verified Successfully!");
    }

    @When("I send a POST request with Invalid payload {string}")
    public void iSendAPOSTRequestWithInvalidPayload(String invalidPayload) {
        LogManager.info("Trying to send post request with invalid payload '"+invalidPayload+"'");
        response=UserServiceController.post(invalidPayload);
        LogManager.info("Request Sent successfully!");
    }


    @And("the request body is:")
    public void theRequestBodyIs(String validPayload) {
        LogManager.info("Trying to send post request with invalid payload '"+validPayload+"'");
        
        response=UserServiceController.post(validPayload,EndPoints.CREATE_USER);
        long responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
        System.out.println("Response Time: " + responseTime + " ms");
        LogManager.info("Request Sent successfully!");

    }

    @And("the response time should be less than {long} ms")
    public void theResponseTimeShouldBeLessThanMs(long expectedResponseTime) {
        LogManager.info("Trying to validate Response time....");
        response.then().assertThat().time(lessThan(expectedResponseTime));
        LogManager.info("Response time validated successfully!");
    }
}
