package com.ats.prompt_review_service.prompt_review.exception;

public class PromptNotFoundException extends RuntimeException {

    public PromptNotFoundException(String message) {
        super(message);
    }
}