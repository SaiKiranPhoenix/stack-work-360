package com.stackwork360.workflowbuilderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.stackwork360")
public class WorkflowBuilderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(WorkflowBuilderServiceApplication.class, args);
    }
}
