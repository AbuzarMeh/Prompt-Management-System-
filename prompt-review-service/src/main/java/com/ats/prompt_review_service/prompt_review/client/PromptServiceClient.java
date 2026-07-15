package com.ats.prompt_review_service.prompt_review.client;

import com.ats.prompt_review_service.prompt_review.dto.response.PromptResponse;
import com.ats.prompt_review_service.prompt_review.exception.PromptNotFoundException;
import com.ats.prompt_review_service.prompt_review.exception.ServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Component
public class PromptServiceClient {

    private final RestTemplate restTemplate;

    @Value("${prompt.service.url}")
    private String promptServiceUrl;

    public PromptServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // public PromptResponse getPrompt(UUID promptId) {

    //     String url = promptServiceUrl + "/" + promptId;
    //     System.out.println("Calling URL: " + url);

    //     try {
    //         PromptResponse response =
    //                 restTemplate.getForObject(url, PromptResponse.class);

    //         System.out.println("Response received: " + response);

    //         return response;

    //     } catch (Exception ex) {

    //         System.out.println("Exception Type: " + ex.getClass().getName());
    //         ex.printStackTrace();

    //         throw ex;
    //     }
    // }
    public PromptResponse getPrompt(UUID promptId) {

        String url = promptServiceUrl + "/" + promptId;
        System.out.println("Calling URL: " + url);

        try {

            PromptResponse response =
                    restTemplate.getForObject(url, PromptResponse.class);

            System.out.println("Response: " + response);

            return response;

        } catch (Exception ex) {

            System.out.println("Exception class: " + ex.getClass().getName());
            ex.printStackTrace();

            throw ex;
        }
    }
}