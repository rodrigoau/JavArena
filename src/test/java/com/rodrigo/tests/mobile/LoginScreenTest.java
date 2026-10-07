package com.rodrigo.tests.mobile;

import com.rodrigo.automation.mobile.screens.HomeScreen;
import com.rodrigo.automation.mobile.screens.LoginScreen;
import com.rodrigo.automation.utils.ConfigReader;
import com.rodrigo.automation.utils.JsonReader;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginScreenTest extends BaseMobileTest {

    @Test
    @Story("Successful login with valid credentials")
    @Description("Verify that the login and signup button is displayed and clickable")
    public void clickOnLoginAndSignUpOption(){
    }

    @Test
    @Story("Successful login with valid credentials (Properties File)")
    @Description("Verifies that a user can enter their credentials, press the login button, and see the welcome screen in the app.")
    public void loginUsingExistingCredentialsPropertiesFile() {
        String user = ConfigReader.getProperty("mobile.valid.username");
        String pass = ConfigReader.getProperty("mobile.valid.password");
        LoginScreen loginScreen = new LoginScreen();
        HomeScreen homeScreen = new HomeScreen();
        homeScreen.clickOnLoginAndSignUpOption();
        loginScreen.enterEmail(user).enterPassword(pass).clickRememberCheckBox().clickLoginButton();
        Assert.assertTrue(loginScreen.verifySuccessAccess(), "Success message is not displayed");
    }

    @Test
    @Story("Successful login with valid credentials (Json File)")
    @Description("Verifies that a user can enter their credentials, press the login button, and see the welcome screen in the app.")
    public void loginUsingExistingCredentialsJsonFile() {
        String user = JsonReader.getString("/mobile/validUser/username");
        String pass = JsonReader.getString("/mobile/validUser/password");
        LoginScreen loginScreen = new LoginScreen();
        HomeScreen homeScreen = new HomeScreen();
        homeScreen.clickOnLoginAndSignUpOption();
        loginScreen.enterEmail(user).enterPassword(pass).clickRememberCheckBox().clickLoginButton();
        Assert.assertTrue(loginScreen.verifySuccessAccess(), "Success message is not displayed");
    }
}