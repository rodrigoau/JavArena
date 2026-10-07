package com.rodrigo.tests.mobile;

import com.rodrigo.automation.mobile.screens.HomeScreen;
import com.rodrigo.automation.mobile.screens.LoginScreen;
import com.rodrigo.automation.utils.ConfigReader;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class LoginScreenTest extends BaseMobileTest {

    @Test
    @Story("Inicio de sesión exitoso con credenciales válidas")
    @Description("Verify that the login and signup button is displayed and clickable")
    public void clickOnLoginAndSignUpOption(){
        HomeScreen homeScreen = new HomeScreen();
        homeScreen.clickOnLoginAndSignUpOption();
    }

    @Test
    @Story("Inicio de sesión exitoso con credenciales válidas")
    @Description("Verifica que un usuario pueda ingresar sus credenciales, presionar el botón de login y ver la pantalla de bienvenida en la app.")
    public void loginUsingExistingCredentials() throws InterruptedException {
        String user = ConfigReader.getProperty("mobile.valid.username");
        String pass = ConfigReader.getProperty("mobile.valid.password");
        LoginScreen loginScreen = new LoginScreen();
        loginScreen.enterEmail(user).enterPassword(pass).clickRememberCheckBox().clickLoginButton();
        Assert.assertTrue(loginScreen.verifySuccessAccess(), "Success message is not displayed");
    }
}
