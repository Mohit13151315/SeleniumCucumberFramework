package com.infosys.framework.utils;

import com.infosys.framework.models.LoginTestData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.InputStream;

public class ExcelTestDataReader {

    private static final String TEST_DATA_FILE = "testdata/LoginTestData.xlsx";

    public LoginTestData getLoginTestData(String scenarioId) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(TEST_DATA_FILE)) {
            if (inputStream == null) {
                throw new RuntimeException("Excel test data file not found: " + TEST_DATA_FILE);
            }

            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheet("LoginData");
            if (sheet == null) {
                throw new RuntimeException("Sheet not found: LoginData");
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                String currentScenario = row.getCell(0).getStringCellValue();
                if (scenarioId.equalsIgnoreCase(currentScenario)) {
                    return new LoginTestData(
                            currentScenario,
                            row.getCell(1).getStringCellValue(),
                            row.getCell(2).getStringCellValue(),
                            row.getCell(3).getStringCellValue(),
                            row.getCell(4).getStringCellValue()
                    );
                }
            }

            throw new RuntimeException("Scenario not found in Excel: " + scenarioId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read Excel test data", e);
        }
    }
}
