package com.stackwork360.workforceplanningservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.stackwork360")
public class WorkforcePlanningServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(WorkforcePlanningServiceApplication.class, args);
    }
}
