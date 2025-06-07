Feature: Posts API CRUD Operations
  As an API user
  I want to perform CRUD operations on posts
  So that I can manage post data effectively

  Background:
    Given the API base URL is configured
    And the API client is initialized

  @smoke @create @Test
  Scenario: Create a new post
    Given I have post data with title "Test Post" and body "Test content"
    When I send a POST request to create the post
    Then the response status code should be 201
    And the response should contain the post details
    And the response should have a valid post ID

  @regression @read
  Scenario: Retrieve a specific post by ID
    Given a post with ID 1 exists
    When I send a GET request for post ID 1
    Then the response status code should be 200
    And the response should contain post details for ID 1

  @regression @read
  Scenario: Retrieve all posts
    When I send a GET request to retrieve all posts
    Then the response status code should be 200
    And the response should contain multiple posts
    And each post should have required fields

  @regression @update
  Scenario: Update an existing post
    Given a post with ID 1 exists
    And I have updated post data with title "Updated Title" and body "Updated content"
    When I send a PUT request to update post ID 1
    Then the response status code should be 200
    And the response should contain the updated post details

  @regression @update
  Scenario: Partially update a post
    Given a post with ID 1 exists
    And I have partial update data with title "Patched Title"
    When I send a PATCH request to update post ID 1
    Then the response status code should be 200
    And the response should contain the patched title

  @regression @delete
  Scenario: Delete a post
    Given a post with ID 1 exists
    When I send a DELETE request for post ID 1
    Then the response status code should be 200

  @negative
  Scenario: Attempt to retrieve non-existent post
    When I send a GET request for post ID 99999
    Then the response status code should be 404

  @negative
  Scenario: Create post with invalid data
    Given I have invalid post data
    When I send a POST request to create the post
    Then the response should handle the invalid data appropriately

  @performance
  Scenario: Verify response time for getting all posts
    When I send a GET request to retrieve all posts
    Then the response status code should be 200
    And the response time should be less than 2000 milliseconds

  @chaining
  Scenario: Request chaining - Create and retrieve post
    Given I have post data with title "Chained Post" and body "Chained content"
    When I send a POST request to create the post
    Then the response status code should be 201
    And I store the created post ID
    When I send a GET request for the stored post ID
    Then the response status code should be 200
    And the response should match the originally created post data