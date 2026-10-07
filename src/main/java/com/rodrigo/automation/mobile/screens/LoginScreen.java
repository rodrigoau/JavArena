package com.rodrigo.automation.mobile.screens;

import com.rodrigo.automation.mobile.BaseMobilePage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class LoginScreen extends BaseMobilePage {
    public LoginScreen(){
        super();
    }

    private final By emailInputLocator = AppiumBy.xpath("//android.widget.EditText[@resource-id='login_email_input']");
    private final By passwordInputLocator = AppiumBy.xpath("//android.widget.EditText[@resource-id='login_password_input']");
    private final By rememberMeCheckBoxLocator = AppiumBy.xpath("//android.widget.CheckBox[@content-desc='Remember me']");
    private final By loginInButtonLocator = AppiumBy.xpath("//android.widget.Button[@content-desc='Log in']");
    private final By welcomeStandardMessageLocator = AppiumBy.xpath("//android.view.View[@content-desc='Welcome, Standard Tester!']");
    public LoginScreen enterEmail(String email) {
        driver.findElement(emailInputLocator).click();
        driver.findElement(emailInputLocator).sendKeys(email);
        return this;
    }
    public LoginScreen enterPassword(String password) {
        driver.findElement(passwordInputLocator).click();
        driver.findElement(passwordInputLocator).sendKeys(password);
        return this;
    }
    public LoginScreen clickRememberCheckBox() {
        driver.findElement(rememberMeCheckBoxLocator).click();
        return this;
    }
    public LoginScreen clickLoginButton() {
        driver.findElement(loginInButtonLocator).click();
        return this;
    }
    public boolean verifySuccessAccess(){
        return driver.findElement(welcomeStandardMessageLocator).isDisplayed();
    }
}
