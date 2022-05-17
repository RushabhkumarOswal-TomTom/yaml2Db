package com.utility.yaml2db.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.utility.yaml2db.config.JdbcConfig;
import com.utility.yaml2db.config.JdbcDemo;
import com.utility.yaml2db.constant.Constants;
import com.utility.yaml2db.model.InputYamlModel;

import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class YamlReaderServiceImpl {

    public  void dump() throws Exception {
        File file = new File(Constants.INPUT_TEST_FILE);
        //     File file = new File(urls);
        URL url = new URL(Constants.urls);
        //split file in chunks
        SplitFileServiceImpl splitFileService = new SplitFileServiceImpl();
        splitFileService.splitFiles(Constants.NUMBER_OF_SPLITS);

        ExecutorService executor = Executors.newFixedThreadPool(Constants.NUMBER_OF_THREADS);
        for (int i = 1; i <= Constants.NUMBER_OF_THREADS; i++) {
            JdbcConfig config = new JdbcConfig();
            Connection connection = config.getConnection();
            Runnable jdbcConnection = new JdbcConnection(i, connection, config);
            executor.execute(jdbcConnection);
        }
    }

    public static class JdbcConnection implements Runnable {
        private int number;
        private Connection connection;
        private JdbcConfig jdbcConfig;

        public JdbcConnection(int number, Connection connection, JdbcConfig jdbcConfig) {
            this.number = number;
            this.connection = connection;
            this.jdbcConfig = jdbcConfig;
        }

        @Override
        public void run() {

            try (BufferedReader br = new BufferedReader(new FileReader(Constants.SPLIT_FILE + number + ".yaml"))) {
                String line;
                while ((line = br.readLine()) != null) {

                    // process the line.
                    // System.out.println(line);
                    InputYamlModel inputYamlModel = getIdModel(line);
                    long threadId = Thread.currentThread().getId();
                    System.out.println("Thread # " + threadId + " is doing this task");
                    System.out.println(inputYamlModel);
                    JdbcDemo jdbcDemo = new JdbcDemo(connection);
                    jdbcDemo.connect(inputYamlModel);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException | SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                jdbcConfig.closeConnection(connection);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private static InputYamlModel getIdModel(String line) {
        int breakPoint = line.indexOf(":");
        InputYamlModel inputYamlModel = new InputYamlModel();
        inputYamlModel.setOsmId(new BigInteger(line.substring(1, breakPoint).replaceAll("^\\s+|\\s+$", "")));
        String seorenIdArray[] = line.substring(breakPoint + 1, line.length()-1).split(",");
        BigInteger[] seorenIdBigArray = new BigInteger[seorenIdArray.length];
        for (int i = 0; i < seorenIdArray.length; i++) {
            seorenIdBigArray[i] = new BigInteger(seorenIdArray[i].trim());
        }
        inputYamlModel.setSeorenId(seorenIdBigArray);
        return inputYamlModel;


    }
}
