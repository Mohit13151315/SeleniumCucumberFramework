package com.infosys.framework.context;

import com.infosys.framework.models.LoginTestData;

public class ScenarioContext {

    private static final ThreadLocal<LoginTestData> loginTestData = new ThreadLocal<>();

    public static void setLoginTestData(LoginTestData data) {
        loginTestData.set(data);
    }

    public static LoginTestData getLoginTestData() {
        return loginTestData.get();
    }

    public static void clear() {
        loginTestData.remove();
    }
}