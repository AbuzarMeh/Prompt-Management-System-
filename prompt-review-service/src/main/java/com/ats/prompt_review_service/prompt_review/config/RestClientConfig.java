package com.ats.prompt_review_service.prompt_review.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient(
            @Value("${prompt.service.url}") String promptServiceUrl) {

        return RestClient.builder()
                .baseUrl(promptServiceUrl)
                .build();
    }
}