package com.stackwork360.compensationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.stackwork360")
public class CompensationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CompensationServiceApplication.class, args);
    }
}
