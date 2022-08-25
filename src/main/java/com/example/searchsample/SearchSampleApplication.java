package com.example.searchsample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class SearchSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchSampleApplication.class, args);
    }

}
