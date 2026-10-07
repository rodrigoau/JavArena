package com.rodrigo.tests.web;

import com.rodrigo.automation.web.DriverFactory;
import com.rodrigo.automation.web.pages.AutomateTheInternet;
import org.testng.Assert;
import org.testng.annotations.Test;

public class KlaudersTest extends BaseTest {

    @Test
    public void testKlauders() {
        String title = DriverFactory.getDriver().getTitle();
        Assert.assertTrue(title.contains("Klauders"), "El título de la página no coincide.");
    }

    @Test
    public void validateTitle(){
        AutomateTheInternet automateTheInternet = new AutomateTheInternet();
        Assert.assertTrue(automateTheInternet.verifyPageTitle(), "Title does not match");
    }

    @Test
    public void validatePaymentProcess(){
        AutomateTheInternet automateTheInternet = new AutomateTheInternet();
        automateTheInternet.clickFirstButton().clickSecondButton().clickThirdButton();
    }
}
