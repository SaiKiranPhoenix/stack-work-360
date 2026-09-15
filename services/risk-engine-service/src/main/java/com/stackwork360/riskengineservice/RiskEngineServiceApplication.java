package com.stackwork360.riskengineservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.stackwork360")
public class RiskEngineServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(RiskEngineServiceApplication.class, args);
    }
}
