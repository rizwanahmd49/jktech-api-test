package com.automation.steps;

import com.automation.clients.ApiClient;
import com.automation.clients.UserServiceController;
import com.automation.constants.EndPoints;
import com.automation.models.Post;
import com.automation.utils.LogManager;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.testng.Assert;

import static org.hamcrest.Matchers.*;

/**
 * Step definitions for Post CRUD operations in Cucumber scenarios
 */
public class PostStepDefinitions {
    
    private ApiClient apiClient;
    private Post testPost;
    private Response response;
    private Integer storedPostId;
    private long responseStartTime;

    @Given("the API base URL is configured")
    public void theApiBaseUrlIsConfigured() {
        LogManager.info("Configuring API base URL");
        // Configuration is handled by ConfigManager automatically
    }
    
    @Given("the API client is initialized")
    public void theApiClientIsInitialized() {
        LogManager.info("Initializing API client");
        apiClient = ApiClient.getInstance();
        Assert.assertNotNull(apiClient, "API client should be initialized");
    }
    
    @Given("I have post data with title {string} and body {string}")
    public void iHavePostDataWithTitleAndBody(String title, String body) {
        LogManager.info("Creating test post with title: " + title);
        testPost = new Post(1, title, body);
    }
    
    @Given("a post with ID {int} exists")
    public void aPostWithIdExists(Integer postId) {
        LogManager.info("Verifying post with ID " + postId + " exists");
        // This step assumes the post exists - in real scenarios, you might want to create it first
        response = apiClient.getRequestSpec()
                .pathParam("id", postId)
                .when()
                .get("/posts/{id}");
        
        if (response.getStatusCode() != 200) {
            LogManager.warn("Post with ID " + postId + " does not exist, but continuing with test");
        }
    }
    
    @Given("I have updated post data with title {string} and body {string}")
    public void iHaveUpdatedPostDataWithTitleAndBody(String title, String body) {
        LogManager.info("Creating updated post data with title: " + title);
        testPost = new Post(1, title, body);
        testPost.setId(1);
    }
    
    @Given("I have partial update data with title {string}")
    public void iHavePartialUpdateDataWithTitle(String title) {
        LogManager.info("Creating partial update data with title: " + title);
        // For PATCH requests, we'll use a simple JSON string
        testPost = new Post();
        testPost.setTitle(title);
    }
    
    @Given("I have invalid post data")
    public void iHaveInvalidPostData() {
        LogManager.info("Creating invalid post data");
        // This represents invalid data structure
        testPost = null;
    }
    
    @When("I send a POST request to create the post")
    public void iSendAPostRequestToCreateThePost() {
        LogManager.info("Sending POST request to create post");
        responseStartTime = System.currentTimeMillis();
        System.out.println("Body: "+testPost);
        if (testPost != null) {
            response = apiClient.getRequestSpec()
                    .body(testPost)
                    .when()
                    .post("/posts");
        } else {
            // For invalid data test
            response = apiClient.getRequestSpec()
                    .body("{\"invalidField\": \"value\"}")
                    .when()
                    .post("/posts");
        }
    }
    
    @When("I send a GET request for post ID {int}")
    public void iSendAGetRequestForPostId(Integer postId) {
        LogManager.info("Sending GET request for post ID: " + postId);
        responseStartTime = System.currentTimeMillis();
        response = apiClient.getRequestSpec()
                .pathParam("id", postId)
                .when()
                .get("/posts/{id}");
    }
    
    @When("I send a GET request to retrieve all posts")
    public void iSendAGetRequestToRetrieveAllPosts() {
        LogManager.info("Sending GET request to retrieve all posts");
        responseStartTime = System.currentTimeMillis();
        
        response = apiClient.getRequestSpec()
                .when()
                .get("/posts");
    }
    
    @When("I send a PUT request to update post ID {int}")
    public void iSendAPutRequestToUpdatePostId(Integer postId) {
        LogManager.info("Sending PUT request to update post ID: " + postId);
        responseStartTime = System.currentTimeMillis();
        
        response = apiClient.getRequestSpec()
                .pathParam("id", postId)
                .body(testPost)
                .when()
                .put("/posts/{id}");
    }
    
    @When("I send a PATCH request to update post ID {int}")
    public void iSendAPatchRequestToUpdatePostId(Integer postId) {
        LogManager.info("Sending PATCH request to update post ID: " + postId);
        responseStartTime = System.currentTimeMillis();
        
        String patchData = "{\"title\": \"" + testPost.getTitle() + "\"}";
        response = apiClient.getRequestSpec()
                .pathParam("id", postId)
                .body(patchData)
                .when()
                .patch("/posts/{id}");
    }
    
    @When("I send a DELETE request for post ID {int}")
    public void iSendADeleteRequestForPostId(Integer postId) {
        LogManager.info("Sending DELETE request for post ID: " + postId);
        responseStartTime = System.currentTimeMillis();
        
        response = apiClient.getRequestSpec()
                .pathParam("id", postId)
                .when()
                .delete("/posts/{id}");
    }
    
    @When("I send a GET request for the stored post ID")
    public void iSendAGetRequestForTheStoredPostId() {
        LogManager.info("Sending GET request for stored post ID: " + storedPostId);
        Assert.assertNotNull(storedPostId, "Stored post ID should not be null");
        
        response = apiClient.getRequestSpec()
                .pathParam("id", storedPostId)
                .when()
                .get("/posts/{id}");
    }
    
    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(Integer expectedStatusCode) {
        LogManager.info("Validating response status code: " + expectedStatusCode);
        Assert.assertNotNull(response, "Response should not be null");
        
        int actualStatusCode = response.getStatusCode();
        Assert.assertEquals(actualStatusCode, expectedStatusCode.intValue(), 
                "Expected status code " + expectedStatusCode + " but got " + actualStatusCode);
    }
    
    @Then("the response should contain the post details")
    public void theResponseShouldContainThePostDetails() {
        LogManager.info("Validating response contains post details");
        
        response.then()
                .body("title", equalTo(testPost.getTitle()))
                .body("body", equalTo(testPost.getBody()))
                .body("userId", equalTo(testPost.getUserId()));
    }
    
    @Then("the response should have a valid post ID")
    public void theResponseShouldHaveAValidPostId() {
        LogManager.info("Validating response has valid post ID");
        
        response.then()
                .body("id", notNullValue())
                .body("id", greaterThan(0));
    }
    
    @Then("the response should contain post details for ID {int}")
    public void theResponseShouldContainPostDetailsForId(Integer postId) {
        LogManager.info("Validating response contains details for post ID: " + postId);
        
        response.then()
                .body("id", equalTo(postId))
                .body("userId", notNullValue())
                .body("title", notNullValue())
                .body("body", notNullValue());
    }
    
    @Then("the response should contain multiple posts")
    public void theResponseShouldContainMultiplePosts() {
        LogManager.info("Validating response contains multiple posts");
        
        response.then()
                .body("size()", greaterThan(0));
        
        Post[] posts = response.as(Post[].class);
        Assert.assertTrue(posts.length > 0, "Response should contain at least one post");
    }
    
    @Then("each post should have required fields")
    public void eachPostShouldHaveRequiredFields() {
        LogManager.info("Validating each post has required fields");
        
        Post[] posts = response.as(Post[].class);
        for (Post post : posts) {
            Assert.assertNotNull(post.getId(), "Post ID should not be null");
            Assert.assertNotNull(post.getUserId(), "User ID should not be null");
            Assert.assertNotNull(post.getTitle(), "Title should not be null");
            Assert.assertNotNull(post.getBody(), "Body should not be null");
        }
    }
    
    @Then("the response should contain the updated post details")
    public void theResponseShouldContainTheUpdatedPostDetails() {
        LogManager.info("Validating response contains updated post details");
        
        response.then()
                .body("title", equalTo(testPost.getTitle()))
                .body("body", equalTo(testPost.getBody()));
    }
    
    @Then("the response should contain the patched title")
    public void theResponseShouldContainThePatchedTitle() {
        LogManager.info("Validating response contains patched title");
        
        response.then()
                .body("title", equalTo(testPost.getTitle()));
    }
    
    @Then("the response should handle the invalid data appropriately")
    public void theResponseShouldHandleTheInvalidDataAppropriately() {
        LogManager.info("Validating response handles invalid data appropriately");
        
        int statusCode = response.getStatusCode();
        // The API might return 201 for liberal acceptance or 400 for validation
        Assert.assertTrue(statusCode == 201 || statusCode == 400 || statusCode == 422, 
                "Status code should be 201, 400, or 422 for invalid data, but got: " + statusCode);
    }
    
    @Then("the response time should be less than {long} milliseconds")
    public void theResponseTimeShouldBeLessThanMilliseconds(Long maxResponseTime) {
        LogManager.info("Validating response time is less than " + maxResponseTime + "ms");
        
        long actualResponseTime = System.currentTimeMillis() - responseStartTime;
        Assert.assertTrue(actualResponseTime < maxResponseTime, 
                "Response time should be less than " + maxResponseTime + "ms but was " + actualResponseTime + "ms");
        
        LogManager.info("Actual response time: " + actualResponseTime + "ms");
    }
    
    @Then("I store the created post ID")
    public void iStoreTheCreatedPostId() {
        LogManager.info("Storing created post ID");
        
        storedPostId = response.jsonPath().getInt("id");
        Assert.assertNotNull(storedPostId, "Created post ID should not be null");
        
        LogManager.info("Stored post ID: " + storedPostId);
    }
    
    @Then("the response should match the originally created post data")
    public void theResponseShouldMatchTheOriginallyCreatedPostData() {
        LogManager.info("Validating response matches originally created post data");
        response.then()
                .body("id", equalTo(storedPostId))
                .body("title", equalTo(testPost.getTitle()))
                .body("body", equalTo(testPost.getBody()));
    }
}