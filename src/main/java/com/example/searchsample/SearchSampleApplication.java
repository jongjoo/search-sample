package com.example.searchsample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 오픈 API 기반으로 검색을 통합하는 Application
 */
@EnableJpaAuditing
@ConfigurationPropertiesScan
@SpringBootApplication
public class SearchSampleApplication {

    /**
     * 오픈 API 기반으로 검색을 통합하는 Application
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(SearchSampleApplication.class, args);
    }

}
