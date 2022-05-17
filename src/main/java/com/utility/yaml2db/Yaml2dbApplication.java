package com.utility.yaml2db;

import com.utility.yaml2db.service.YamlReaderServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Yaml2dbApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Yaml2dbApplication.class, args);
		YamlReaderServiceImpl yaml2dbApplication=new YamlReaderServiceImpl();
		yaml2dbApplication.dump();
	}

}
