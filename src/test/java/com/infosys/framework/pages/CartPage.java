package com.infosys.framework.pages;

import com.infosys.framework.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage {

    private final WaitUtils waitUtils;

    private final By checkoutButton = By.id("checkout");

    public CartPage(WebDriver driver) {
        this.waitUtils = new WaitUtils(driver);
    }

    public void clickCheckout() {
        waitUtils.clickable(checkoutButton).click();
    }
}
