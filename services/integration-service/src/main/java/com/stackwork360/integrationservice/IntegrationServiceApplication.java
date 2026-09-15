package com.stackwork360.integrationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.stackwork360")
public class IntegrationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(IntegrationServiceApplication.class, args);
    }
}
