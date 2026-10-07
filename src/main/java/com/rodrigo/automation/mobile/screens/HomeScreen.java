package com.rodrigo.automation.mobile.screens;

import com.rodrigo.automation.mobile.BaseMobilePage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class HomeScreen extends BaseMobilePage {
    public HomeScreen(){
        super();
    }

    private final By authCardLocator = AppiumBy.xpath("//android.widget.Button[@resource-id='home_card_auth']");

    public void clickOnLoginAndSignUpOption(){
        waitForElementVisible(authCardLocator);
        driver.findElement(authCardLocator).click();
    }


}
