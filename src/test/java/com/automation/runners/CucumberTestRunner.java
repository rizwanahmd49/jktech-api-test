package com.automation.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Cucumber Test Runner for BDD scenarios
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.automation.steps"},
//        tags = "@smoke or @regression",
        tags = "@ReqresApiServices",
        plugin = {
                "pretty",
                "html:target/cucumber-reports.html",
                "json:target/cucumber.json",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
               /* "pretty",
               "html:target/cucumber-reports.html",*/

//             "json:target/cucumber-reports/Cucumber.json",
//              "junit:target/cucumber-reports/Cucumber.xml",
//                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
//                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        },
        monochrome = true,
        publish = true
)
public class CucumberTestRunner {
    // This class serves as the entry point for Cucumber tests
}