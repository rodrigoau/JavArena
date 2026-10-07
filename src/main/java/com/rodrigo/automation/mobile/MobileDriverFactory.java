package com.rodrigo.automation.mobile;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.WebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;

public class MobileDriverFactory {

    private static final ThreadLocal<AppiumDriver> mobileDriverThreadLocal = new ThreadLocal<>();

    public static void initMobileDriver(String platformName, String deviceName, String appPath) {
        if (mobileDriverThreadLocal.get() == null) {
            try {
                AppiumDriver driver;
                URL serverUrl = URI.create("http://127.0.0.1:4723").toURL();
                if (platformName.equals("Android")){
                    UiAutomator2Options options = new UiAutomator2Options();
                    options.setDeviceName(deviceName);
                    options.setPlatformName(platformName);
                    options.setAutomationName("UiAutomator2");
                    options.setAppWaitDuration(Duration.ofSeconds(30));
                    options.setAppWaitActivity("*");
                    if (appPath != null && !appPath.isEmpty()) {
                        options.setApp(appPath);
                    } else {

                    }
                    driver = new AndroidDriver(serverUrl, options);
                } else {
                    throw new IllegalArgumentException("Plataforma móvil no soportada: " + platformName);
                }

                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                mobileDriverThreadLocal.set(driver);

            } catch (MalformedURLException e) {
                throw new RuntimeException("La URL del servidor de Appium es inválida: " + e.getMessage());
            }
        }
    }

    public static AppiumDriver getDriver() {
        return mobileDriverThreadLocal.get();
    }

    public static void quitDriver() {
        if (mobileDriverThreadLocal.get() != null) {
            mobileDriverThreadLocal.get().quit();
            mobileDriverThreadLocal.remove();
        }
    }

}
