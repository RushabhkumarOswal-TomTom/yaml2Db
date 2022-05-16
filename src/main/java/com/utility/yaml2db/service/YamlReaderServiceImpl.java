package com.utility.yaml2db.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.utility.yaml2db.config.JdbcConfig;
import com.utility.yaml2db.config.JdbcDemo;
import com.utility.yaml2db.model.InputYamlModel;

import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class YamlReaderServiceImpl {
    static String urls="https://stsdocli.file.core.windows.net/fs-sdo-cli-pipeline/202203252200-bulk-way-sectioned.2022.03.210.WLD.lookup.txt?sv=2020-08-04&ss=f&srt=sco&sp=r&se=2022-05-17T19:19:47Z&st=2022-05-16T11:19:47Z&spr=https&sig=evYPG2C7QcKJYC4ulWEjn%2FYkmqUuLJ%2BXNsJY%2BBiQOL0%3D";

    public static void main(String[] args) throws Exception {
        File file = new File("/Users/oswalr/Documents/yaml2db/yaml2db/src/main/resources/test.yaml");
   //     File file = new File(urls);
        URL url = new URL(urls);
        JdbcConfig config=new JdbcConfig();
        Connection connection=config.getConnection();
        ExecutorService executor = Executors.newFixedThreadPool(21);
        for (int i = 1; i <= 21; i++) {
            Runnable jdbcConnection=new JdbcConnection(i,connection,config);
            executor.execute(jdbcConnection);
        }
    }

 public static  class JdbcConnection implements Runnable{
        private int number;
        private Connection connection;
        private JdbcConfig jdbcConfig;
        public JdbcConnection(int number,Connection connection,JdbcConfig jdbcConfig){
           this.number=number;
           this.connection=connection;
           this.jdbcConfig=jdbcConfig;
        }

        @Override
        public void run() {

            try (BufferedReader br = new BufferedReader(new FileReader("/Users/oswalr/Documents/yaml2db/input/split-"+number+".yaml"))) {
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

    private static InputYamlModel getIdModel(String line)
    {
        int breakPoint=line.indexOf(":");
        InputYamlModel inputYamlModel=new InputYamlModel();
        inputYamlModel.setOsmId(new BigInteger(line.substring(0,breakPoint)));
        String seorenIdArray[]=line.substring(breakPoint+1,line.length()).split(",");
        BigInteger[] seorenIdBigArray=new BigInteger[seorenIdArray.length];
        for (int i=0;i<seorenIdArray.length;i++)
        {
            seorenIdBigArray[i]=new BigInteger(seorenIdArray[i].trim());
        }
        inputYamlModel.setSeorenId(seorenIdBigArray);
return inputYamlModel;


    }
}
