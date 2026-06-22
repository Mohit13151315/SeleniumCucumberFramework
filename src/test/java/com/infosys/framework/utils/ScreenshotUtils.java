package com.infosys.framework.utils;

import com.infosys.framework.reports.ExtentReportManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtils {

    public static String capture(WebDriver driver, String scenarioName) {
        try {
            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String safeName = scenarioName.replaceAll("[^a-zA-Z0-9-_]", "_");
            Path destination = Paths.get(
                    ExtentReportManager.getReportDirectoryPath(),
                    "screenshots",
                    safeName + "_" + timestamp + ".png"
            );
            Files.createDirectories(destination.getParent());
            Files.copy(source.toPath(), destination);
            return destination.toAbsolutePath().toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to capture screenshot", e);
        }
    }
}
