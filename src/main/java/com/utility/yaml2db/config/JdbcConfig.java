package com.utility.yaml2db.config;

import com.utility.yaml2db.constant.Constants;

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

    private final String conUrl = Constants.DATABASE_CONNECTION_URL;

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
