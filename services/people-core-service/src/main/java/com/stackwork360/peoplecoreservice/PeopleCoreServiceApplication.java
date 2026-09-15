package com.stackwork360.peoplecoreservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.stackwork360")
public class PeopleCoreServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PeopleCoreServiceApplication.class, args);
    }
}
