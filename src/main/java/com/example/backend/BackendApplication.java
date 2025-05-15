package com.example.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.example.backend.property.FileStorageProperties;


@SpringBootApplication
@EnableConfigurationProperties({
    FileStorageProperties.class
})

@ConfigurationPropertiesScan("com.example.backend.property") // Add this
public class BackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}