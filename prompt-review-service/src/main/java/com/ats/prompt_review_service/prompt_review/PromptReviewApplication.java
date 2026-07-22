package com.ats.prompt_review_service.prompt_review;

import com.ats.prompt_review_service.prompt_review.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class PromptReviewApplication {

    public static void main(String[] args) {
        SpringApplication.run(PromptReviewApplication.class, args);
    }
}