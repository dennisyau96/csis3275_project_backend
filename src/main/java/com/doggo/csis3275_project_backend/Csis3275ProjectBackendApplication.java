package com.doggo.csis3275_project_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
public class Csis3275ProjectBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(Csis3275ProjectBackendApplication.class, args);
    }

}
