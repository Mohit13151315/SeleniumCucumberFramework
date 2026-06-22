@all_scenarios @login
Feature: Login functionality

  @smoke @TC_VALID_LOGIN
  Scenario: Verify valid login
    Given I launch the application
    When I login with test data
    Then products page should be displayed

  @regression @TC_LOCKED_OUT_USER
  Scenario: Verify locked out user login
    Given I launch the application
    When I login with test data
    Then locked out user error message should be displayed

  @regression @TC_INVALID_LOGIN
  Scenario: Verify invalid login error message
    Given I launch the application
    When I login with test data
    Then invalid login error message should be displayed
