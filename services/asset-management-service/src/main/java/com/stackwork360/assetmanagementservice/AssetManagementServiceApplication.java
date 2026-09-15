package com.stackwork360.assetmanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.stackwork360")
public class AssetManagementServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AssetManagementServiceApplication.class, args);
    }
}
