package dev.sebsven.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.client.RestClient;

@Configuration
public class ClientConfiguration {

    @Bean
    public RestClient trivialRestClientBuilder() {
        return RestClient.builder()
                .baseUrl("https://opentdb.com/api.php")
                .build();
    }
}
