package com.rodrigo.automation.mobile.screens;

import com.rodrigo.automation.mobile.BaseMobilePage;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomeScreen extends BaseMobilePage {
    public HomeScreen(){
        super();
    }

    private final By authCardLocator = AppiumBy.xpath("//android.widget.Button[@resource-id='home_card_auth']");

    public void clickOnLoginAndSignUpOption(){
        waitForElementVisible(authCardLocator);
        driver.findElement(authCardLocator).click();
    }

    // 1) IMPLICIT WAIT
    // Se configura globalmente en el driver: cada findElement reintenta hasta el timeout
    // si el elemento no existe. Solo espera "presencia", no visibilidad ni clickabilidad.
    // No se recomienda mezclarlo con explicit waits, por eso se resetea a 0 al final.
    public void clickOnAuthCardWithImplicitWait(){
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        try {
            driver.findElement(authCardLocator).click();
        } finally {
            driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        }
    }

    // 2) EXPLICIT WAIT
    // Espera una condición concreta para un elemento concreto (aquí: que sea clickable).
    // Hace polling cada 500 ms por defecto.
    public void clickOnAuthCardWithExplicitWait(){
        WebDriverWait explicitWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement authCard = explicitWait.until(ExpectedConditions.elementToBeClickable(authCardLocator));
        authCard.click();
    }

    // 3) FLUENT WAIT
    // Como el explicit wait, pero permite configurar timeout, frecuencia de polling,
    // excepciones a ignorar y mensaje de error.
    public void clickOnAuthCardWithFluentWait(){
        Wait<WebDriver> fluentWait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(15))
                .pollingEvery(Duration.ofMillis(300))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .withMessage("La tarjeta de autenticación no apareció en la Home");

        WebElement authCard = fluentWait.until(d -> {
            WebElement element = d.findElement(authCardLocator);
            return element.isDisplayed() && element.isEnabled() ? element : null;
        });
        authCard.click();
    }

}
