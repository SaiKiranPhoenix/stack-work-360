package com.stackwork360.benefitsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.stackwork360")
public class BenefitsServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BenefitsServiceApplication.class, args);
    }
}
