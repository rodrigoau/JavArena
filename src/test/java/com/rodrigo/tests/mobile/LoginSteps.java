package com.rodrigo.tests.mobile;

import com.rodrigo.automation.mobile.MobileDriverFactory;
import com.rodrigo.automation.mobile.screens.HomeScreen;
import com.rodrigo.automation.mobile.screens.LoginScreen;
import com.rodrigo.automation.utils.ConfigReader;
import com.rodrigo.automation.utils.JsonReader;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class LoginSteps extends BaseMobileTest {

    private LoginScreen loginScreen;
    private HomeScreen homeScreen;

    @Given("I open the mobile application")
    public void iOpenTheMobileApplication() {
        Assert.assertNotNull(MobileDriverFactory.getDriver(), "El driver no se inicializó correctamente");
    }

    @When("I enter my valid username and password from config")
    public void iEnterMyValidUsernameAndPasswordFromConfig() {
        String user = ConfigReader.getProperty("mobile.valid.username");
        String pass = ConfigReader.getProperty("mobile.valid.password");
        loginScreen = new LoginScreen();
        loginScreen.enterEmail(user).enterPassword(pass).clickRememberCheckBox();
    }

    @When("I enter my credentials from the JSON file and submit them")
    public void iEnterMyCredentialsFromTheJsonFileAndSubmitThem() {
        String user = JsonReader.getString("/mobile/validUser/username");
        String pass = JsonReader.getString("/mobile/validUser/password");

        homeScreen = new HomeScreen();
        homeScreen.clickOnLoginAndSignUpOption();

        loginScreen = new LoginScreen();
        loginScreen.enterEmail(user).enterPassword(pass).clickRememberCheckBox().clickLoginButton();
    }

    @And("I click the login button")
    public void iClickTheLoginButton() {
        loginScreen.clickLoginButton();
    }

    @Then("I should see the success access message")
    public void iShouldSeeTheSuccessAccessMessage() {
        boolean isSuccess = loginScreen.verifySuccessAccess();
        Assert.assertTrue(isSuccess, "El mensaje de éxito no es visible en la pantalla");
    }

    @After
    public void tearDown() {
        MobileDriverFactory.quitDriver();
    }
}