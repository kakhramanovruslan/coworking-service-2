package org.example.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing database connections using JDBC.
 */
public class ConnectionManager {

    private final String URL;
    private final String USERNAME;
    private final String PASSWORD;

    public ConnectionManager(String url, String username, String password, String driver) {
        this.URL = url;
        this.USERNAME = username;
        this.PASSWORD = password;

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method for establishing connection with database.
     * @return Connection class which may be used to work with database.
     */
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get a database connection.", e);
        }
    }
}