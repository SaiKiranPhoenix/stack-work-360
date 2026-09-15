package com.stackwork360.tenantservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.stackwork360")
public class TenantServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TenantServiceApplication.class, args);
    }
}
