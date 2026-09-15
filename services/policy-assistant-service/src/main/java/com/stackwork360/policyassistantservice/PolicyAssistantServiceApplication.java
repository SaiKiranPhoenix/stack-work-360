package com.stackwork360.policyassistantservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.stackwork360")
public class PolicyAssistantServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PolicyAssistantServiceApplication.class, args);
    }
}
