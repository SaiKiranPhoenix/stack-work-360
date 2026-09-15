package com.stackwork360.goalsokrservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.stackwork360")
public class GoalsOkrServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoalsOkrServiceApplication.class, args);
    }
}
