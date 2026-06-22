package com.infosys.framework.pages;

import com.infosys.framework.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    private final WebDriver driver;
    private final WaitUtils waitUtils;

    private final By usernameInput = By.id("user-name");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By productsTitle = By.className("title");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
    }

    public void navigateToApplication(String url) {
        driver.get(url);
    }

    public void login(String username, String password) {
        waitUtils.visible(usernameInput).sendKeys(username);
        waitUtils.visible(passwordInput).sendKeys(password);
        waitUtils.clickable(loginButton).click();
    }

    public String getProductsTitle() {
        return waitUtils.visible(productsTitle).getText();
    }

    public String getErrorMessage() {
        return waitUtils.visible(errorMessage).getText();
    }

    public String getLoginButtonText() {
        return waitUtils.visible(loginButton).getAttribute("value");
    }
}
