package com.infosys.framework.pages;

import com.infosys.framework.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage {

    private final WaitUtils waitUtils;

    private final By firstNameInput = By.id("first-name");
    private final By lastNameInput = By.id("last-name");
    private final By postalCodeInput = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By finishButton = By.id("finish");
    private final By confirmationMessage = By.className("complete-header");

    public CheckoutPage(WebDriver driver) {
        this.waitUtils = new WaitUtils(driver);
    }

    public void enterCheckoutInformation(String firstName, String lastName, String postalCode) {
        waitUtils.visible(firstNameInput).sendKeys(firstName);
        waitUtils.visible(lastNameInput).sendKeys(lastName);
        waitUtils.visible(postalCodeInput).sendKeys(postalCode);
        waitUtils.clickable(continueButton).click();
    }

    public void finishCheckout() {
        waitUtils.clickable(finishButton).click();
    }

    public String getConfirmationMessage() {
        return waitUtils.visible(confirmationMessage).getText();
    }
}
