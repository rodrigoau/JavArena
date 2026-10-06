package com.rodrigo.tests.web;

import com.rodrigo.automation.web.DriverFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class KlaudersTest extends BaseTest {

    @Test
    public void testKlauders() {
        String title = DriverFactory.getDriver().getTitle();
        Assert.assertTrue(title.contains("Klauders"), "El título de la página no coincide.");
    }
}
