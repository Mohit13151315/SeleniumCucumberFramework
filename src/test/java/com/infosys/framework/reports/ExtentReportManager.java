package com.infosys.framework.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExtentReportManager {

    private static ExtentReports extentReports;
    private static String reportPath;
    private static String reportDirectoryPath;
    private static final String RUN_TIMESTAMP = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

    private ExtentReportManager() {
    }

    public static synchronized ExtentReports getReport() {
        if (extentReports == null) {
            String tagName = getTagNameForReport();
            Path reportDirectory = Paths.get("reports", "extent-reports", tagName + "_" + RUN_TIMESTAMP);

            try {
                Files.createDirectories(reportDirectory);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create Extent report folder", e);
            }

            reportDirectoryPath = reportDirectory.toString();
            reportPath = reportDirectory.resolve("AutomationReport.html").toString();

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setDocumentTitle("Selenium Cucumber Automation Report");
            sparkReporter.config().setReportName("Infosys Interview Demo Framework - " + tagName);

            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);
            extentReports.setSystemInfo("Framework", "Selenium Java Cucumber Maven");
            extentReports.setSystemInfo("Report Type", "Extent HTML Report");
            extentReports.setSystemInfo("Tag", tagName);
            extentReports.setSystemInfo("Run Timestamp", RUN_TIMESTAMP);
            extentReports.setSystemInfo("Environment", System.getProperty("env", "qa"));
            extentReports.setSystemInfo("Browser", System.getProperty("browser", "edge"));
        }
        return extentReports;
    }

    public static synchronized void flushReport() {
        if (extentReports != null) {
            extentReports.flush();
        }
    }

    public static String getReportPath() {
        return reportPath;
    }

    public static String getReportDirectoryPath() {
        if (reportDirectoryPath == null) {
            getReport();
        }
        return reportDirectoryPath;
    }

    private static String getTagNameForReport() {
        String tagExpression = System.getProperty("cucumber.filter.tags", "all");
        return tagExpression
                .replace("@", "")
                .replaceAll("[^a-zA-Z0-9]+", "_")
                .replaceAll("^_+|_+$", "")
                .toLowerCase();
    }
}
