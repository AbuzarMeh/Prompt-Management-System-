package com.ats.prompt_review_service.prompt_review.client;

import com.ats.prompt_review_service.prompt_review.dto.response.PromptResponse;
import com.ats.prompt_review_service.prompt_review.exception.PromptNotFoundException;
import com.ats.prompt_review_service.prompt_review.exception.ServiceUnavailableException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class PromptServiceClient {

    private final RestTemplate restTemplate;
    private final HttpServletRequest request;

    @Value("${prompt.service.url}")
    private String promptServiceUrl;

    public PromptServiceClient(
            RestTemplate restTemplate,
            HttpServletRequest request
    ) {
        this.restTemplate = restTemplate;
        this.request = request;
    }

    public PromptResponse getPrompt(UUID promptId) {
        String url = promptServiceUrl + "/" + promptId;

        HttpHeaders headers = new HttpHeaders();

        String authorizationHeader =
                request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null
                && authorizationHeader.startsWith("Bearer ")) {
            headers.set(HttpHeaders.AUTHORIZATION, authorizationHeader);
        }

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<PromptResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    PromptResponse.class
            );

            return response.getBody();
        } catch (HttpStatusCodeException ex) {
            if (ex.getStatusCode().value() == HttpStatus.NOT_FOUND.value()) {
                throw new PromptNotFoundException(
                        "Prompt not found with id: " + promptId
                );
            }

            throw new ServiceUnavailableException(
                    "Prompt service returned " + ex.getStatusCode().value()
                            + " for prompt id: " + promptId
            );
        } catch (ResourceAccessException ex) {
            throw new ServiceUnavailableException(
                    "Prompt service is unavailable."
            );
        } catch (RestClientException ex) {
            throw new ServiceUnavailableException(
                    "Prompt service request failed."
            );
        }
    }
}