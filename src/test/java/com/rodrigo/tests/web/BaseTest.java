package com.rodrigo.tests.web;

import com.rodrigo.automation.web.DriverFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

public class BaseTest {

    @BeforeMethod
    @Parameters({"browser"})
    public void setUp(String browser) {
        String selectedBrowser = (browser != null) ? browser : "chrome";
        DriverFactory.initDriver(selectedBrowser);
        DriverFactory.getDriver().get("https://klauders.com/automate-the-internet");
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
