package com.rodrigo.tests.web;

import com.rodrigo.automation.web.DriverFactory;
import org.testng.annotations.*;

public class BaseTest {

    @BeforeSuite
    @Parameters({"browser"})
    public void setUp(String browser) {
        String selectedBrowser = (browser != null) ? browser : "chrome";
        DriverFactory.initDriver(selectedBrowser);
        DriverFactory.getDriver().get("https://klauders.com/automate-the-internet");
    }

    @AfterSuite
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
