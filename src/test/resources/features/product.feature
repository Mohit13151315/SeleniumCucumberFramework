@all_scenarios @product
Feature: Product and checkout functionality

  Background:
    Given I launch the application
    When I login with valid credentials

  @smoke @TC_ADD_PRODUCT_CART
  Scenario: Verify user can add a product to cart
    When I add a product to the cart
    Then cart badge should show expected product count

  @regression @TC_REMOVE_PRODUCT
  Scenario: Verify user can remove a product from cart
    When I add a product to the cart
    And I remove the product from the cart
    Then cart badge should show expected product count

  @regression @TC_CHECKOUT_ORDER
  Scenario: Verify user can complete checkout
    When I add a product to the cart
    And I checkout the product
    Then order confirmation message should be displayed

  @smoke @TC_LOGOUT_USER
  Scenario: Verify user can logout successfully
    When I logout from the application
    Then login page should be displayed
