# Selenium Cucumber Framework

Interview-ready Selenium Java Cucumber Maven framework for UI automation.

This framework demonstrates a real-time automation flow using Cucumber feature files, Page Object Model, Excel-driven scenario data, DB-driven credentials, Extent HTML reports, screenshots, Maven parameters, and Jenkins-ready execution.

## Tech Stack

- Java 17
- Selenium WebDriver
- Cucumber BDD
- JUnit 4 Runner
- Maven
- Apache POI for Excel test data
- H2 database through JDBC for credential lookup
- ExtentReports for HTML reporting
- Selenium Grid support for remote execution
- Jenkins-ready Maven commands

## Project Structure

```text
src/test/java/com/infosys/framework
  context
    ScenarioContext.java
  factory
    DriverFactory.java
  hooks
    Hooks.java
  models
    Credentials.java
    LoginTestData.java
  pages
    LoginPage.java
    ProductsPage.java
    CartPage.java
    CheckoutPage.java
  reports
    ExtentReportManager.java
    ExtentTestManager.java
  runners
    TestRunner.java
  stepdefinitions
    LoginSteps.java
  utils
    ConfigReader.java
    DBUtils.java
    ExcelTestDataReader.java
    ScreenshotUtils.java
    WaitUtils.java

src/test/resources
  config/config.properties
  db/schema.sql
  features/login.feature
  features/product.feature
  testdata/LoginTestData.xlsx
```

## Feature Files

### login.feature

Contains login-specific scenarios:

- valid login
- locked out user login
- invalid login

These scenarios launch the application and perform login directly because login itself is being tested.

### product.feature

Contains product, cart, checkout, and logout scenarios.

It uses Cucumber `Background` for common business precondition:

```gherkin
Background:
  Given I launch the application
  When I login with valid credentials
```

This means the application launch and login steps run before every scenario in `product.feature`.

## Execution Flow

```text
Maven/Jenkins command
  -> TestRunner
  -> Cucumber feature file
  -> Hooks @Before(order = 0)
       -> create Extent test
       -> read scenario id from @TC_ tag
       -> read scenario data from Excel
       -> check runMode
       -> initialize WebDriver
  -> LoginSteps @Before(order = 1)
       -> create Page Object classes
  -> Feature step execution
       -> Step definition calls page class methods
       -> Page class uses Selenium WebDriver
       -> Assertions validate expected result
  -> Hooks @After
       -> capture screenshot on failure
       -> update Extent report
       -> close browser
```

## Important Classes

### TestRunner.java

JUnit 4 Cucumber runner. It tells Cucumber where feature files and step definitions are present.

### Hooks.java

Handles technical setup and cleanup:

- starts Extent report test
- reads Excel test data based on scenario tag
- skips scenario when Excel `runMode` is `N`
- launches browser using `DriverFactory`
- captures screenshot on failure
- closes browser after scenario
- flushes report

### LoginSteps.java

Contains Cucumber step definitions. It connects feature file steps with Page Object methods.

### DriverFactory.java

Creates local or remote browser driver based on runtime parameters:

- `browser=edge`
- `browser=chrome`
- `executionMode=local`
- `executionMode=remote`

### ConfigReader.java

Reads values from `config.properties` and also supports runtime override from Maven command.

Example:

```cmd
-Denv=qa
-Dbrowser=edge
```

### ExcelTestDataReader.java

Reads scenario data from:

```text
src/test/resources/testdata/LoginTestData.xlsx
```

Scenario is matched using `@TC_` tag.

Example:

```gherkin
@TC_VALID_LOGIN
```

maps to Excel row:

```text
VALID_LOGIN
```

### DBUtils.java

Fetches username and password from H2 database using credential key from Excel.

### Page Classes

Page Object classes store locators and reusable page actions:

- `LoginPage`
- `ProductsPage`
- `CartPage`
- `CheckoutPage`

## Maven Commands

Run smoke tests:

```cmd
mvn clean test -Denv=qa -Dbrowser=edge "-Dcucumber.filter.tags=@smoke"
```

Run regression tests:

```cmd
mvn clean test -Denv=qa -Dbrowser=edge "-Dcucumber.filter.tags=@regression"
```

Run login scenarios:

```cmd
mvn clean test -Denv=qa -Dbrowser=edge "-Dcucumber.filter.tags=@login"
```

Run product scenarios:

```cmd
mvn clean test -Denv=qa -Dbrowser=edge "-Dcucumber.filter.tags=@product"
```

Run one specific scenario:

```cmd
mvn clean test -Denv=qa -Dbrowser=edge "-Dcucumber.filter.tags=@TC_VALID_LOGIN"
```

Run on Chrome:

```cmd
mvn clean test -Denv=qa -Dbrowser=chrome "-Dcucumber.filter.tags=@smoke"
```

Run on Selenium Grid:

```cmd
mvn clean test -Denv=qa -Dbrowser=edge -DexecutionMode=remote "-Dcucumber.filter.tags=@smoke"
```

## Jenkins Maven Goal

For a parameterized Jenkins freestyle job:

```cmd
clean test -Denv=%env% -Dbrowser=%browser% -DexecutionMode=%executionMode% -Dcucumber.filter.tags="%tags%"
```

Suggested Jenkins parameters:

- `env`: `qa`, `uat`
- `browser`: `edge`, `chrome`
- `executionMode`: `local`, `remote`
- `tags`: `@smoke`, `@regression`, `@login`, `@product`, `@TC_VALID_LOGIN`

## Reports

Extent HTML report is generated automatically after test execution:

```text
reports/extent-reports/<tag>_<timestamp>/AutomationReport.html
```

Screenshots for failed scenarios are stored under the same report folder.

## Interview Explanation

This is a Selenium Java Cucumber framework built using Page Object Model. Execution starts from Maven or Jenkins, which triggers the Cucumber JUnit runner. The runner reads feature files and executes matching scenarios based on tags. Hooks handle technical setup like report creation, Excel test data loading, driver initialization, screenshot capture, and browser cleanup. Step definitions connect business-readable Gherkin steps with Java code. Page classes contain locators and reusable Selenium actions. Test data comes from Excel, credentials come from DB, and final execution evidence is generated in Extent HTML report.
