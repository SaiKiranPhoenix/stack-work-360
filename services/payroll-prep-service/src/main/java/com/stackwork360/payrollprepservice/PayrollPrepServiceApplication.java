package com.stackwork360.payrollprepservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.stackwork360")
public class PayrollPrepServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayrollPrepServiceApplication.class, args);
    }
}
