package com.stackwork360.performanceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.stackwork360")
public class PerformanceServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PerformanceServiceApplication.class, args);
    }
}
