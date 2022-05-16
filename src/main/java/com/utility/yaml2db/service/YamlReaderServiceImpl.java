package com.utility.yaml2db.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.utility.yaml2db.config.JdbcConfig;
import com.utility.yaml2db.config.JdbcDemo;
import com.utility.yaml2db.model.InputYamlModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Connection;

public class YamlReaderServiceImpl {
    static String urls="https://stsdocli.file.core.windows.net/fs-sdo-cli-pipeline/202203252200-bulk-way-sectioned.2022.03.210.WLD.lookup.txt?sv=2020-08-04&ss=f&srt=sco&sp=r&se=2022-05-17T19:19:47Z&st=2022-05-16T11:19:47Z&spr=https&sig=evYPG2C7QcKJYC4ulWEjn%2FYkmqUuLJ%2BXNsJY%2BBiQOL0%3D";

    public static void main(String[] args) throws Exception {
        File file = new File("/Users/oswalr/Documents/yaml2db/yaml2db/src/main/resources/test.yaml");
   //     File file = new File(urls);
        URL url = new URL(urls);
        JdbcConfig config=new JdbcConfig();
        Connection connection=config.getConnection();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                // process the line.
               // System.out.println(line);
             InputYamlModel inputYamlModel = getIdModel(line);
                System.out.println(inputYamlModel);
                JdbcDemo jdbcDemo=new JdbcDemo(connection);
                jdbcDemo.connect(inputYamlModel);
            }
        }
        config.closeConnection(connection);
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
