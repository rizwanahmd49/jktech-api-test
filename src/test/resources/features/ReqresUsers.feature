@ReqresApiServices
Feature: ReqRes API Comprehensive Test Coverage
  Base URL: https://reqres.in

  Background:
    Given I set the base URL to "https://reqres.in"



  Scenario: Get list of Users
    When I send GET request to retrieve all users
    Then I should receive a response with status code 200
    And the response should contain a non-empty list of users
    And the response should contain minimum of 12 fields

  Scenario: Single User
    When I send GET request to retrieve user details
    Then I should receive a response with status code 200
    And the response should contain user with id 2
    And the response should contain field "email" and value "janet.weaver@reqres.in"


  Scenario: Single User Not Found
    When I send GET request to "users/23" endpoint
    Then I should receive a response with status code 404


  Scenario: Create User
    When I send a POST request to CREATE user with the following data:
      | name | job       |
      | morpheus | leader |
    Then I should receive a response with status code 201
    And the response should contain field "id" and value should  not be NULL
    And POST or PUT response should contain field "name" and value "morpheus" in response body
    And POST or PUT response should contain field "job" and value "leader" in response body


  Scenario: Update User
    When I send a PUT request to CREATE user with the following data:
      | name | job       |
      | morpheus | zion resident |
    Then I should receive a response with status code 200
    And POST or PUT response should contain field "name" and value "morpheus" in response body


  Scenario: Delete User
    When I send a DELETE request to remove the user
    Then I should receive a response with status code 204

  @Resources
  Scenario: Request chaining - Complete user lifecycle - Create, Read, Update, Delete
    When I send a POST request to CREATE user with the following data:
      | name | job       |
      | morpheus | leader |
    Then I should receive a response with status code 201
    And the response should contain field "id" and value should  not be NULL
    And And I store the created post data for validation
    When I send GET request to retrieve user details
    Then I should receive a response with status code 200
    And Validat the created user id from stored data
    When I send a DELETE request to remove the user
    Then I should receive a response with status code 204


