package com.infosys.framework.factory;

import com.infosys.framework.utils.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    public static void initDriver() {
        String browser = ConfigReader.getRuntimeValue("browser");
        String executionMode = ConfigReader.getRuntimeValue("executionMode");

        WebDriver driver;
        if ("remote".equalsIgnoreCase(executionMode)) {
            driver = createRemoteDriver(browser);
        } else {
            driver = createLocalDriver(browser);
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Integer.parseInt(ConfigReader.get("implicitWait"))));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(Integer.parseInt(ConfigReader.get("pageLoadTimeout"))));
        DRIVER.set(driver);
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new RuntimeException("Driver is not initialized for current thread");
        }
        return driver;
    }

    public static boolean isDriverInitialized() {
        return DRIVER.get() != null;
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }

    private static WebDriver createLocalDriver(String browser) {
        if ("chrome".equalsIgnoreCase(browser)) {
            return new ChromeDriver();
        }
        if ("edge".equalsIgnoreCase(browser)) {
            return new EdgeDriver();
        }
        throw new RuntimeException("Unsupported local browser: " + browser);
    }

    private static WebDriver createRemoteDriver(String browser) {
        try {
            URL gridUrl = new URL(ConfigReader.get("grid.url"));

            if ("chrome".equalsIgnoreCase(browser)) {
                return new RemoteWebDriver(gridUrl, new ChromeOptions());
            }

            if ("edge".equalsIgnoreCase(browser)) {
                return new RemoteWebDriver(gridUrl, new EdgeOptions());
            }

            throw new RuntimeException("Unsupported remote browser: " + browser);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Selenium Grid URL", e);
        }
    }
}
