package com.automation.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class ReqResStepDef {


    @Given("the base URI is set to {string}")
    public void theBaseURIIsSetTo(String arg0) {


    }
    @When("I send a GET request to {string} with query parameter {string} = {string}")
    public void iSendAGETRequestToWithQueryParameter(String arg0, String arg1, String arg2) {


    }

    @And("every item in JSON path {string} should match the regex {string}")
    public void everyItemInJSONPathShouldMatchTheRegex(String arg0, String arg1) {
    }

}
