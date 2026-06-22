package com.infosys.framework.hooks;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.infosys.framework.context.ScenarioContext;
import com.infosys.framework.factory.DriverFactory;
import com.infosys.framework.models.LoginTestData;
import com.infosys.framework.reports.ExtentReportManager;
import com.infosys.framework.reports.ExtentTestManager;
import com.infosys.framework.utils.ConfigReader;
import com.infosys.framework.utils.ExcelTestDataReader;
import com.infosys.framework.utils.ScreenshotUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.Status;
import org.junit.Assume;

public class Hooks {

    private final ExcelTestDataReader excelTestDataReader = new ExcelTestDataReader();

    @Before(order = 0)
    public void beforeScenario(Scenario scenario) {
        ExtentTestManager.setTest(ExtentReportManager.getReport().createTest(scenario.getName()));
        ExtentTestManager.info("Scenario started: " + scenario.getName());

        String scenarioId = getScenarioIdFromTag(scenario);
        LoginTestData testData = excelTestDataReader.getLoginTestData(scenarioId);
        ScenarioContext.setLoginTestData(testData);

        ExtentTestManager.info("Scenario ID: " + scenarioId);
        ExtentTestManager.info("Run mode from Excel: " + testData.getRunMode());

        Assume.assumeTrue("Scenario skipped because runMode is set to N in Excel for scenarioId: " + scenarioId, testData.getRunMode().equalsIgnoreCase("Y"));

        DriverFactory.initDriver();
    }

    @After
    public void afterScenario(Scenario scenario) {
        try {
            if (scenario.isFailed() && DriverFactory.isDriverInitialized()) {
                String screenshotPath = ScreenshotUtils.capture(DriverFactory.getDriver(), scenario.getName());

                ExtentTestManager.getTest().fail(
                        "Scenario failed",
                        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build()
                );
            } else if (scenario.getStatus() == Status.SKIPPED) {
                ExtentTestManager.getTest().skip("Scenario skipped");
            } else {
                ExtentTestManager.getTest().pass("Scenario passed");
            }
        } finally {
            waitBeforeClosingBrowserIfConfigured();
            DriverFactory.quitDriver();
            ExtentReportManager.flushReport();
            ExtentTestManager.removeTest();
            ScenarioContext.clear();
        }
    }

    private String getScenarioIdFromTag(Scenario scenario) {
        return scenario.getSourceTagNames()
                .stream()
                .filter(tag -> tag.startsWith("@TC_"))
                .findFirst()
                .map(tag -> tag.replace("@TC_", ""))
                .orElseThrow(() -> new RuntimeException(
                        "Missing test case tag. Please add a tag like @TC_VALID_LOGIN to scenario: "
                                + scenario.getName()
                ));
    }

    private void waitBeforeClosingBrowserIfConfigured() {
        int debugWaitInSeconds = Integer.parseInt(ConfigReader.getRuntimeValue("debugWaitInSeconds"));

        if (debugWaitInSeconds > 0) {
            try {
                Thread.sleep(debugWaitInSeconds * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Debug wait was interrupted", e);
            }
        }
    }
}
