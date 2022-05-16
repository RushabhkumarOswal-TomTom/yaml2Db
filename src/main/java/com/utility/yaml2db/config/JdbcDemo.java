package com.utility.yaml2db.config;

import com.utility.yaml2db.model.InputYamlModel;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Properties;

public class JdbcDemo {
    private Connection connection;

    public JdbcDemo(Connection connection) {
        this.connection = connection;
    }


    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private final String conUrl = "jdbc:postgresql://lead-store.postgres.database.azure.com:5432/test-yaml2db?ssl=true&sslmode=require";


    public void connect(InputYamlModel inputYamlModel) throws IOException, SQLException, ClassNotFoundException {
        System.setProperty("java.rmi.server.hostname", "192.168.1.2");


        insert(inputYamlModel);
    }


    public long insert(InputYamlModel inputYamlModel) throws IOException, SQLException {
        String SQLinsert = "INSERT INTO osm2seroen(osm,seroen) "
                + "VALUES(?,?)";

        long id = 0;


        try (
                PreparedStatement prepareStatement = connection.prepareStatement(SQLinsert, Statement.RETURN_GENERATED_KEYS)) {

            String typeName = JDBCType.BIGINT.getName();
            Array array = connection.createArrayOf(typeName, inputYamlModel.getSeorenId());
            prepareStatement.setBigDecimal(1, new BigDecimal(inputYamlModel.getOsmId()));
            prepareStatement.setArray(2, array);

            int rowsAffected = prepareStatement.executeUpdate();

            if (rowsAffected > 0) {

                try (ResultSet rs = prepareStatement.getGeneratedKeys()) {
                    if (rs.next()) {
                        id = rs.getLong(1);
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }


        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return id;
    }
}