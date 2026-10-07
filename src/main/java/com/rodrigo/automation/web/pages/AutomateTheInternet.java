package com.rodrigo.automation.web.pages;

import com.rodrigo.automation.web.BasePage;
import org.openqa.selenium.By;

public class AutomateTheInternet extends BasePage {

    public AutomateTheInternet() {
        super();
    }

    private final By headerTitleLocator = By.xpath("//span[@data-i18n='pg_title']");

    public boolean verifyPageTitle(){
        boolean itPassed = false;
        if (driver.findElement(headerTitleLocator).isDisplayed() && driver.findElement(headerTitleLocator).getText().equals("Practice automation on real-world UI patterns")) {
            itPassed = true;
        }
        return itPassed;
    }

    public AutomateTheInternet clickFirstButton(){
        driver.findElement(headerTitleLocator).click();
        return this;
    }

    public AutomateTheInternet clickSecondButton(){
        driver.findElement(headerTitleLocator).click();
        return this;
    }

    public AutomateTheInternet clickThirdButton(){
        driver.findElement(headerTitleLocator).click();
        return this;
    }
}
