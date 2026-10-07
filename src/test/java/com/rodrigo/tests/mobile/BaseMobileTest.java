package com.rodrigo.tests.mobile;

import com.rodrigo.automation.mobile.MobileDriverFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class BaseMobileTest {
    @BeforeMethod
    @Parameters({"platformName", "deviceName", "appPath"})
    public void setUp(@Optional("Android") String platformName,
                      @Optional("AndroidEmulator") String deviceName,
                      @Optional("") String appPath){
        MobileDriverFactory.initMobileDriver(platformName, deviceName, appPath);
    }

    @AfterMethod
    public void tearDown(){
        MobileDriverFactory.quitDriver();
    }
}
