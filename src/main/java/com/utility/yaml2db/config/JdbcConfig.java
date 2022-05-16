package com.utility.yaml2db.config;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcConfig {
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private final String conUrl = "jdbc:postgresql://lead-store.postgres.database.azure.com:5432/test-yaml2db?ssl=true&sslmode=require";

    public Connection getConnection() throws IOException, SQLException {
        Properties properties = new Properties();
        properties.load(JdbcDemo.class.getClassLoader().getResourceAsStream("application.properties"));
        Connection connection = DriverManager.getConnection(conUrl, properties);
        System.out.println("Database connection test: " + connection.getCatalog());
        return connection;
    }

    public void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }
}
