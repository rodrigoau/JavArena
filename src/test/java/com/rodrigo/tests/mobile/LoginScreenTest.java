package com.rodrigo.tests.mobile;

import com.rodrigo.automation.mobile.screens.HomeScreen;
import com.rodrigo.automation.mobile.screens.LoginScreen;
import io.qameta.allure.Description;
import io.qameta.allure.Story;
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
        LoginScreen loginScreen = new LoginScreen();
        loginScreen.enterEmail("tester@automation.com").enterPassword("Test1234!").clickRememberCheckBox().clickLoginButton();
    }
}
