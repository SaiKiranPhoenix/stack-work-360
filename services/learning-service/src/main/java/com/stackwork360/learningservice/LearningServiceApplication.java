package com.stackwork360.learningservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.stackwork360")
public class LearningServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(LearningServiceApplication.class, args);
    }
}
