package com.stackwork360.leaveservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.stackwork360")
public class LeaveServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(LeaveServiceApplication.class, args);
    }
}
