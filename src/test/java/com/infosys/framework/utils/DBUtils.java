package com.infosys.framework.utils;

import com.infosys.framework.models.Credentials;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBUtils {

    public Credentials getCredentials(String credentialKey) {
        String query = "SELECT USERNAME, PASSWORD FROM LOGIN_CREDENTIALS WHERE CREDENTIAL_KEY = ?";

        try (Connection connection = DriverManager.getConnection(
                ConfigReader.get("db.url"),
                ConfigReader.get("db.username"),
                ConfigReader.getOptional("db.password"));
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, credentialKey);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Credentials(
                            resultSet.getString("USERNAME"),
                            resultSet.getString("PASSWORD")
                    );
                }
            }

            throw new RuntimeException("Credentials not found in DB for key: " + credentialKey);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch credentials from DB", e);
        }
    }
}
