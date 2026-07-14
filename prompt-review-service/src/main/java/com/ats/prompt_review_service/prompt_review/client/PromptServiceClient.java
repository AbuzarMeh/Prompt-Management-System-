package com.ats.prompt_review_service.prompt_review.client;

import com.ats.prompt_review_service.prompt_review.dto.PromptResponse;
import com.ats.prompt_review_service.prompt_review.exception.PromptNotFoundException;
import com.ats.prompt_review_service.prompt_review.exception.ServiceUnavailableException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.HttpClientErrorException;

import java.util.UUID;

@Component
public class PromptServiceClient {

    private final RestClient restClient;

    public PromptServiceClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public PromptResponse getPrompt(UUID promptId) {

        try {

            return restClient.get()
                    .uri("/api/prompts/{id}", promptId)
                    .retrieve()
                    .body(PromptResponse.class);

        } catch (RestClientResponseException ex) {

            if (ex.getStatusCode().value() == 404) {
                throw new PromptNotFoundException(
                        "Prompt not found with id: " + promptId
                );
            }

            throw ex;
        } catch (ResourceAccessException ex) {

            throw new ServiceUnavailableException(
                    "Prompt Service is unavailable"
            );
        }
    }
}