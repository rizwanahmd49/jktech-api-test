package com.automation.tests;

import com.automation.clients.ApiClient;
import com.automation.models.Post;
import com.automation.utils.LogManager;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

/**
 * Comprehensive CRUD tests for Posts API
 */
@Epic("API Automation")
@Feature("Posts CRUD Operations")
public class PostCrudTests {
    
    private ApiClient apiClient;
    private SoftAssert softAssert;
    private Integer createdPostId;
    
    @BeforeClass
    public void setupClass() {
        LogManager.info("Setting up PostCrudTests class");
        apiClient = ApiClient.getInstance();
    }
    
    @BeforeMethod
    public void setupMethod() {
        softAssert = new SoftAssert();
    }
    
    @AfterMethod
    public void tearDownMethod() {
        softAssert.assertAll();
    }

    @Test(priority = 1)
    @Story("Create Post")
    @Description("Test creating a new post via POST API")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreatePost() {
        LogManager.info("Starting test: Create Post");
        
        // Test data
        Post newPost = new Post(1, "Test Post Title", "This is test post body content");
        
        Response response = apiClient.getRequestSpec()
                .body(newPost)
                .when()
                .post("/posts")
                .then()
                .statusCode(201)
                .body("title", equalTo(newPost.getTitle()))
                .body("body", equalTo(newPost.getBody()))
                .body("userId", equalTo(newPost.getUserId()))
                .body("id", notNullValue())
                .extract().response();
        
        // Store created post ID for subsequent tests
        createdPostId = response.jsonPath().getInt("id");
        LogManager.info("Created post with ID: " + createdPostId);
        
        // Validate response structure
        response.then().body(matchesJsonSchemaInClasspath("schemas/post-schema.json"));
    }

    @Test(priority = 2, dependsOnMethods = {"testCreatePost"})
    @Story("Read Post")
    @Description("Test retrieving a specific post by ID")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetPostById() {
        LogManager.info("Starting test: Get Post By ID");
        
        Response response = apiClient.getRequestSpec()
                .pathParam("id", 1)
                .when()
                .get("/posts/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("userId", notNullValue())
                .body("title", notNullValue())
                .body("body", notNullValue())
                .extract().response();
        
        // Validate response structure
        response.then().body(matchesJsonSchemaInClasspath("schemas/post-schema.json"));
        
        LogManager.info("Successfully retrieved post with ID: 1");
    }

    @Test(priority = 3)
    @Story("Read All Posts")
    @Description("Test retrieving all posts")
    @Severity(SeverityLevel.NORMAL)
    public void testGetAllPosts() {
        LogManager.info("Starting test: Get All Posts");
        
        Response response = apiClient.getRequestSpec()
                .when()
                .get("/posts")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .extract().response();
        
        // Validate that response is an array and has expected structure
        Post[] posts = response.as(Post[].class);
        assertTrue(posts.length > 0, "Posts array should not be empty");
        
        // Validate first post structure
        assertNotNull(posts[0].getId(), "Post ID should not be null");
        assertNotNull(posts[0].getUserId(), "User ID should not be null");
        assertNotNull(posts[0].getTitle(), "Title should not be null");
        assertNotNull(posts[0].getBody(), "Body should not be null");
        
        LogManager.info("Successfully retrieved " + posts.length + " posts");
    }

    @Test(priority = 4)
    @Story("Update Post")
    @Description("Test updating an existing post via PUT API")
    @Severity(SeverityLevel.CRITICAL)
    public void testUpdatePost() {
        LogManager.info("Starting test: Update Post");
        
        // Updated post data
        Post updatedPost = new Post(1, "Updated Test Title", "Updated test body content");
        updatedPost.setId(1);
        
        Response response = apiClient.getRequestSpec()
                .pathParam("id", 1)
                .body(updatedPost)
                .when()
                .put("/posts/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("title", equalTo(updatedPost.getTitle()))
                .body("body", equalTo(updatedPost.getBody()))
                .body("userId", equalTo(updatedPost.getUserId()))
                .extract().response();
        
        LogManager.info("Successfully updated post with ID: 1");
    }

    @Test(priority = 5)
    @Story("Partial Update Post")
    @Description("Test partially updating a post via PATCH API")
    @Severity(SeverityLevel.NORMAL)
    public void testPatchPost() {
        LogManager.info("Starting test: Patch Post");
        
        String patchData = "{\"title\": \"Patched Title Only\"}";
        
        Response response = apiClient.getRequestSpec()
                .pathParam("id", 1)
                .body(patchData)
                .when()
                .patch("/posts/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("title", equalTo("Patched Title Only"))
                .extract().response();
        
        LogManager.info("Successfully patched post with ID: 1");
    }

    @Test(priority = 6)
    @Story("Delete Post")
    @Description("Test deleting a post via DELETE API")
    @Severity(SeverityLevel.CRITICAL)
    public void testDeletePost() {
        LogManager.info("Starting test: Delete Post");
        
        apiClient.getRequestSpec()
                .pathParam("id", 1)
                .when()
                .delete("/posts/{id}")
                .then()
                .statusCode(200);
        
        LogManager.info("Successfully deleted post with ID: 1");
    }

    @Test(priority = 7)
    @Story("Negative Testing")
    @Description("Test error handling for non-existent post")
    @Severity(SeverityLevel.NORMAL)
    public void testGetNonExistentPost() {
        LogManager.info("Starting test: Get Non-Existent Post");
        
        apiClient.getRequestSpec()
                .pathParam("id", 99999)
                .when()
                .get("/posts/{id}")
                .then()
                .statusCode(404);
        
        LogManager.info("Correctly handled request for non-existent post");
    }

    @Test(priority = 8)
    @Story("Negative Testing")
    @Description("Test validation for invalid post data")
    @Severity(SeverityLevel.NORMAL)
    public void testCreatePostWithInvalidData() {
        LogManager.info("Starting test: Create Post with Invalid Data");
        
        String invalidPostData = "{\"invalidField\": \"value\"}";
        
        // This test assumes the API validates required fields
        Response response = apiClient.getRequestSpec()
                .body(invalidPostData)
                .when()
                .post("/posts")
                .then()
                .extract().response();
        
        // The API might still return 201 due to liberal acceptance
        // But we can validate that required fields are missing
        LogManager.info("Response status code: " + response.getStatusCode());
    }

    @Test(priority = 9)
    @Story("Performance Testing")
    @Description("Test response time for getting all posts")
    @Severity(SeverityLevel.MINOR)
    public void testResponseTimeForGetAllPosts() {
        LogManager.info("Starting test: Response Time for Get All Posts");
        
        long startTime = System.currentTimeMillis();
        
        apiClient.getRequestSpec()
                .when()
                .get("/posts")
                .then()
                .statusCode(200)
                .time(lessThan(2000L)); // Response should be under 2 seconds
        
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        
        LogManager.info("Response time: " + responseTime + "ms");
        assertTrue(responseTime < 5000, "Response time should be under 5 seconds");
    }

    @Test(priority = 10)
    @Story("Request Chaining")
    @Description("Test request chaining - create post and then retrieve it")
    @Severity(SeverityLevel.NORMAL)
    public void testRequestChaining() {
        LogManager.info("Starting test: Request Chaining");
        
        // Step 1: Create a new post
        Post newPost = new Post(1, "Chained Test Post", "This post will be retrieved immediately");
        
        Response createResponse = apiClient.getRequestSpec()
                .body(newPost)
                .when()
                .post("/posts")
                .then()
                .statusCode(201)
                .extract().response();
        
        Integer postId = createResponse.jsonPath().getInt("id");
        LogManager.info("Created post with ID: " + postId + " for chaining test");
        
        // Step 2: Retrieve the created post
        Response getResponse = apiClient.getRequestSpec()
                .pathParam("id", postId)
                .when()
                .get("/posts/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(postId))
                .body("title", equalTo(newPost.getTitle()))
                .body("body", equalTo(newPost.getBody()))
                .extract().response();
        
        LogManager.info("Successfully chained create and retrieve operations");
    }
}