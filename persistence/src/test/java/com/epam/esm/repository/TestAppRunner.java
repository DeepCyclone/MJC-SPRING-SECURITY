package com.epam.esm.repository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.epam.esm"})
public class TestAppRunner {
    public static void main(String[] args) {
        SpringApplication.run(TestAppRunner.class, args);

    }
}
