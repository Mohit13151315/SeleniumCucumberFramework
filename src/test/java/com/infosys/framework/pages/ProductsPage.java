package com.infosys.framework.pages;

import com.infosys.framework.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductsPage {

    private final WebDriver driver;
    private final WaitUtils waitUtils;

    private final By addBackpackButton = By.id("add-to-cart-sauce-labs-backpack");
    private final By removeBackpackButton = By.id("remove-sauce-labs-backpack");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By cartLink = By.className("shopping_cart_link");
    private final By menuButton = By.id("react-burger-menu-btn");
    private final By logoutLink = By.id("logout_sidebar_link");

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
    }

    public void addBackpackToCart() {
        waitUtils.clickable(addBackpackButton).click();
    }

    public void removeBackpackFromCart() {
        waitUtils.clickable(removeBackpackButton).click();
    }

    public String getCartCount() {
        if (driver.findElements(cartBadge).isEmpty()) {
            return "0";
        }
        return waitUtils.visible(cartBadge).getText();
    }

    public void openCart() {
        waitUtils.clickable(cartLink).click();
    }

    public void logout() {
        waitUtils.clickable(menuButton).click();
        waitUtils.clickable(logoutLink).click();
    }
}