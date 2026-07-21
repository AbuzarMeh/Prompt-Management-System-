package com.ats.prompt_review_service.prompt_review.client;

import com.ats.prompt_review_service.prompt_review.dto.response.PromptResponse;
import com.ats.prompt_review_service.prompt_review.exception.PromptNotFoundException;
import com.ats.prompt_review_service.prompt_review.exception.ServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class PromptServiceClient {

    private final RestTemplate restTemplate;

    @Value("${prompt.service.url}")
    private String promptServiceUrl;

    public PromptServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PromptResponse getPrompt(UUID promptId) {

        String url = promptServiceUrl + "/" + promptId;

        try {
            return restTemplate.getForObject(url, PromptResponse.class);
        } catch (HttpStatusCodeException ex) {
            if (ex.getStatusCode().value() == HttpStatus.NOT_FOUND.value()) {
                throw new PromptNotFoundException("Prompt not found with id: " + promptId);
            }

            throw new ServiceUnavailableException(
                    "Prompt service returned " + ex.getStatusCode().value()
                            + " for prompt id: " + promptId
            );
        } catch (ResourceAccessException ex) {
            throw new ServiceUnavailableException("Prompt service is unavailable.");
        } catch (RestClientException ex) {
            throw new ServiceUnavailableException("Prompt service request failed.");
        }
    }
}
