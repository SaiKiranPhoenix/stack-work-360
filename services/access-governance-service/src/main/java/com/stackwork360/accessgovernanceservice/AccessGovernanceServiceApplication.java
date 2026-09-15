package com.stackwork360.accessgovernanceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.stackwork360")
public class AccessGovernanceServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccessGovernanceServiceApplication.class, args);
    }
}
