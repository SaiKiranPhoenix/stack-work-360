package com.stackwork360.shiftschedulingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.stackwork360")
public class ShiftSchedulingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShiftSchedulingServiceApplication.class, args);
    }
}
