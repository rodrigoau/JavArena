package com.rodrigo.tests.web;

import com.rodrigo.automation.web.DriverFactory;
import com.rodrigo.automation.web.pages.AutomateTheInternet;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Módulo Web")
@Feature("Automatización de Interfaz")
public class KlaudersTest extends BaseTest {

    @Test
    @Story("Validar título del navegador")
    @Description("Verifica mediante el título de la pestaña del navegador que la página de Klauders ha cargado correctamente.")
    public void testKlaudersTitle() {
        String title = DriverFactory.getDriver().getTitle();
        Assert.assertTrue(title.contains("Klauders"), "El título de la página no coincide con el esperado.");
    }

    @Test
    @Story("Validar título mediante Page Object")
    @Description("Verifica el título de la página utilizando los métodos encapsulados en el Page Object de AutomateTheInternet.")
    public void validateTitle() {
        AutomateTheInternet automateTheInternet = new AutomateTheInternet();
        Assert.assertTrue(automateTheInternet.verifyPageTitle(), "El título validado por el Page Object no coincide.");
    }

    @Test
    @Story("Prueba de encadenamiento de métodos (Method Chaining)")
    @Description("Ejecuta una secuencia de clics encadenados simulando un flujo de navegación o proceso en la interfaz.")
    public void validatePaymentProcess() {
        AutomateTheInternet automateTheInternet = new AutomateTheInternet();

        // Ejecución fluida aplicando Method Chaining
        automateTheInternet.clickFirstButton()
                .clickSecondButton()
                .clickThirdButton();
    }
}


class login2Page {

    public void verifyLoginPage(){

    }
}