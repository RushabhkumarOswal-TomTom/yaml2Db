package com.utility.yaml2db.constant;

public interface Constants {
    long NUMBER_OF_SPLITS = 21;
    int NUMBER_OF_THREADS = (int) NUMBER_OF_SPLITS + 1;
    String urls = "https://stsdocli.file.core.windows.net/fs-sdo-cli-pipeline/202203252200-bulk-way-sectioned.2022.03.210.WLD.lookup.txt?sv=2020-08-04&ss=f&srt=sco&sp=r&se=2022-05-17T19:19:47Z&st=2022-05-16T11:19:47Z&spr=https&sig=evYPG2C7QcKJYC4ulWEjn%2FYkmqUuLJ%2BXNsJY%2BBiQOL0%3D";
    String INPUT_TEST_FILE = "/Users/oswalr/Documents/yaml2db/yaml2db/src/main/resources/test.yaml";
    String SPLIT_FILE = "/Users/oswalr/Documents/yaml2db/input/split-";
    String DATABASE_CONNECTION_URL = "jdbc:postgresql://lead-store.postgres.database.azure.com:5432/test-yaml2db?ssl=true&sslmode=require";

    String INPUT_SPLIT_FILE = "/Users/oswalr/Documents/yaml2db/input/202203252200-bulk-way-sectioned.2022.03.210.WLD.lookup.txt";


}
