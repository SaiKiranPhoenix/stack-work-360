package com.stackwork360.skillsgraphservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.stackwork360")
public class SkillsGraphServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SkillsGraphServiceApplication.class, args);
    }
}
