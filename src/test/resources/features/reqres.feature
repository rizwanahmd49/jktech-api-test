Feature: Validate Reqres.in User API Responses

  Background:
    Given the base URI is set to "https://reqres.in/api"

  @smoke
  Scenario: Validate status code and Content-Type header for GET /users/2
    When I send a GET request to "/users/2"
    Then the response status code should be 200
    And the "Content-Type" header should contain "application/json"

  @regression
  Scenario: Validate single user email for GET /users/2
    When I send a GET request to "/users/2"
    Then the response status code should be 200
    And the response JSON path "data.email" should be "janet.weaver@reqres.in"

  @regression
  Scenario: Validate all returned emails end with "@reqres.in" for GET /users?page=2
    When I send a GET request to "/users" with query parameter "page" = "2"
    Then the response status code should be 200
    And every item in JSON path "data.email" should match the regex "^[A-Za-z0-9._%+-]+@reqres\\.in$"