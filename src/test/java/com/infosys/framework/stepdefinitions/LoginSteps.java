package com.infosys.framework.stepdefinitions;

import com.infosys.framework.context.ScenarioContext;
import com.infosys.framework.factory.DriverFactory;
import com.infosys.framework.models.Credentials;
import com.infosys.framework.models.LoginTestData;
import com.infosys.framework.pages.CartPage;
import com.infosys.framework.pages.CheckoutPage;
import com.infosys.framework.pages.LoginPage;
import com.infosys.framework.pages.ProductsPage;
import com.infosys.framework.utils.ConfigReader;
import com.infosys.framework.utils.DBUtils;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

public class LoginSteps {

    private LoginPage loginPage;
    private ProductsPage productsPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    private LoginTestData testData;

    @Before(order = 1)
    public void initializeStepObjects() {
        testData = ScenarioContext.getLoginTestData();
        loginPage = new LoginPage(DriverFactory.getDriver());
        productsPage = new ProductsPage(DriverFactory.getDriver());
        cartPage = new CartPage(DriverFactory.getDriver());
        checkoutPage = new CheckoutPage(DriverFactory.getDriver());
    }

    @Given("I launch the application")
    public void iLaunchTheApplication() {
        loginPage.navigateToApplication(ConfigReader.getEnvironmentUrl());
    }

    @When("I login with test data")
    public void iLoginWithTestData() {
        DBUtils dbUtils = new DBUtils();
        Credentials credentials = dbUtils.getCredentials(testData.getCredentialKey());
        loginPage.login(credentials.getUsername(), credentials.getPassword());
    }

    @When("I login with valid credentials")
    public void iLoginWithValidCredentials() {
        iLoginWithTestData();
    }

    @When("I add a product to the cart")
    public void iAddAProductToTheCart() {
        productsPage.addBackpackToCart();
    }

    @When("I remove the product from the cart")
    public void iRemoveTheProductFromTheCart() {
        productsPage.removeBackpackFromCart();
    }

    @When("I checkout the product")
    public void iCheckoutTheProduct() {
        productsPage.openCart();
        cartPage.clickCheckout();
        checkoutPage.enterCheckoutInformation("Mohit", "Kurmi", "411001");
        checkoutPage.finishCheckout();
    }

    @When("I logout from the application")
    public void iLogoutFromTheApplication() {
        productsPage.logout();
    }

    @Then("products page should be displayed")
    public void productsPageShouldBeDisplayed() {
        Assert.assertEquals(testData.getExpectedMessage(), loginPage.getProductsTitle());
    }

    @Then("locked out user error message should be displayed")
    public void lockedOutUserErrorMessageShouldBeDisplayed() {
        Assert.assertEquals(testData.getExpectedMessage(), loginPage.getErrorMessage());
    }

    @Then("invalid login error message should be displayed")
    public void invalidLoginErrorMessageShouldBeDisplayed() {
        Assert.assertEquals(testData.getExpectedMessage(), loginPage.getErrorMessage());
    }

    @Then("cart badge should show expected product count")
    public void cartBadgeShouldShowExpectedProductCount() {
        Assert.assertEquals(testData.getExpectedMessage(), productsPage.getCartCount());
    }

    @Then("order confirmation message should be displayed")
    public void orderConfirmationMessageShouldBeDisplayed() {
        Assert.assertEquals(testData.getExpectedMessage(), checkoutPage.getConfirmationMessage());
    }

    @Then("login page should be displayed")
    public void loginPageShouldBeDisplayed() {
        Assert.assertEquals(testData.getExpectedMessage(), loginPage.getLoginButtonText());
    }
}
