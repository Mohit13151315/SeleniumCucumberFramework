package com.infosys.framework.models;

public class LoginTestData {
    private final String scenarioId;
    private final String runMode;
    private final String credentialKey;
    private final String expectedType;
    private final String expectedMessage;

    public LoginTestData(String scenarioId, String runMode, String credentialKey, String expectedType, String expectedMessage) {
        this.scenarioId = scenarioId;
        this.runMode = runMode;
        this.credentialKey = credentialKey;
        this.expectedType = expectedType;
        this.expectedMessage = expectedMessage;
    }

    public String getScenarioId() {
        return scenarioId;
    }

    public String getRunMode() {
        return runMode;
    }

    public String getCredentialKey() {
        return credentialKey;
    }

    public String getExpectedType() {
        return expectedType;
    }

    public String getExpectedMessage() {
        return expectedMessage;
    }
}
