package com.stackwork360.speakupcaseservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.stackwork360")
public class SpeakUpCaseServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpeakUpCaseServiceApplication.class, args);
    }
}
