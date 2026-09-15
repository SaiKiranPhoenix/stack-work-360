package com.stackwork360.helpdeskservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.stackwork360")
public class HelpdeskServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelpdeskServiceApplication.class, args);
    }
}
