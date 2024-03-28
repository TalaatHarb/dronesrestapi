package com.mohamedsamir1495.dronesrestapi.config;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", glue = "com.mohamedsamir1495.dronesrestapi")
public class CucumberIntegrationTest {
}
