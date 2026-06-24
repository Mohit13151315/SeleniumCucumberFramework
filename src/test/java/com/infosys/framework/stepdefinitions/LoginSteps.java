package com.infosys.framework.stepdefinitions;

import com.infosys.framework.context.ScenarioContext;
import com.infosys.framework.factory.DriverFactory;
import com.infosys.framework.models.Credentials;
import com.infosys.framework.models.LoginTestData;
import com.infosys.framework.pages.CartPage;
import com.infosys.framework.pages.CheckoutPage;
import com.infosys.framework.pages.LoginPage;
import com.infosys.framework.pages.ProductsPage;
import com.infosys.framework.reports.ExtentTestManager;
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
        ExtentTestManager.info("Initializing page objects for scenario");
        testData = ScenarioContext.getLoginTestData();
        loginPage = new LoginPage(DriverFactory.getDriver());
        productsPage = new ProductsPage(DriverFactory.getDriver());
        cartPage = new CartPage(DriverFactory.getDriver());
        checkoutPage = new CheckoutPage(DriverFactory.getDriver());
        ExtentTestManager.info("Page objects initialized successfully");
    }

    @Given("I launch the application")
    public void iLaunchTheApplication() {
        ExtentTestManager.info("Launching application URL: " + ConfigReader.getEnvironmentUrl());
        loginPage.navigateToApplication(ConfigReader.getEnvironmentUrl());
    }

    @When("I login with test data")
    public void iLoginWithTestData() {
        ExtentTestManager.info("Reading login credentials from database using key: " + testData.getCredentialKey());
        DBUtils dbUtils = new DBUtils();
        Credentials credentials = dbUtils.getCredentials(testData.getCredentialKey());
        ExtentTestManager.info("Performing login");
        loginPage.login(credentials.getUsername(), credentials.getPassword());
    }

    @When("I login with valid credentials")
    public void iLoginWithValidCredentials() {
        ExtentTestManager.info("Logging in with valid credentials from common background flow");
        iLoginWithTestData();
    }

    @When("I add a product to the cart")
    public void iAddAProductToTheCart() {
        ExtentTestManager.info("Adding product to cart");
        productsPage.addBackpackToCart();
    }

    @When("I remove the product from the cart")
    public void iRemoveTheProductFromTheCart() {
        ExtentTestManager.info("Removing product from cart");
        productsPage.removeBackpackFromCart();
    }

    @When("I checkout the product")
    public void iCheckoutTheProduct() {
        ExtentTestManager.info("Opening cart and starting checkout");
        productsPage.openCart();
        cartPage.clickCheckout();
        ExtentTestManager.info("Entering checkout information");
        checkoutPage.enterCheckoutInformation("Mohit", "Kurmi", "411001");
        ExtentTestManager.info("Finishing checkout");
        checkoutPage.finishCheckout();
    }

    @When("I logout from the application")
    public void iLogoutFromTheApplication() {
        ExtentTestManager.info("Logging out from application");
        productsPage.logout();
    }

    @Then("products page should be displayed")
    public void productsPageShouldBeDisplayed() {
        ExtentTestManager.info("Validating products page title");
        Assert.assertEquals(testData.getExpectedMessage(), loginPage.getProductsTitle());
    }

    @Then("locked out user error message should be displayed")
    public void lockedOutUserErrorMessageShouldBeDisplayed() {
        ExtentTestManager.info("Validating locked out user error message");
        Assert.assertEquals(testData.getExpectedMessage(), loginPage.getErrorMessage());
    }

    @Then("invalid login error message should be displayed")
    public void invalidLoginErrorMessageShouldBeDisplayed() {
        ExtentTestManager.info("Validating invalid login error message");
        Assert.assertEquals(testData.getExpectedMessage(), loginPage.getErrorMessage());
    }

    @Then("cart badge should show expected product count")
    public void cartBadgeShouldShowExpectedProductCount() {
        ExtentTestManager.info("Validating cart badge count");
        Assert.assertEquals(testData.getExpectedMessage(), productsPage.getCartCount());
    }

    @Then("order confirmation message should be displayed")
    public void orderConfirmationMessageShouldBeDisplayed() {
        ExtentTestManager.info("Validating order confirmation message");
        Assert.assertEquals(testData.getExpectedMessage(), checkoutPage.getConfirmationMessage());
    }

    @Then("login page should be displayed")
    public void loginPageShouldBeDisplayed() {
        ExtentTestManager.info("Validating login page is displayed");
        Assert.assertEquals(testData.getExpectedMessage(), loginPage.getLoginButtonText());
    }
}
