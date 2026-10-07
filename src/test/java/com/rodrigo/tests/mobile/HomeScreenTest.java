package com.rodrigo.tests.mobile;

import com.rodrigo.automation.mobile.screens.HomeScreen;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

@Epic("Módulo Móvil Android")
@Feature("Autenticación de Usuarios")
public class HomeScreenTest extends BaseMobileTest{

    @Test
    @Story("Inicio de sesión exitoso con credenciales válidas")
    @Description("Verifica que un usuario pueda ingresar sus credenciales, presionar el botón de login y ver la pantalla de bienvenida en la app.")
    public void clickOnLoginAndSignUpOption(){
        HomeScreen homeScreen = new HomeScreen();
        homeScreen.clickOnLoginAndSignUpOption();
    }

}
