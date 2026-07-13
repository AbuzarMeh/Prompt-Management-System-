package com.ats.prompt_service.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ErrorResponse", description = "Standard API error payload")
public class ErrorResponse {

    @Schema(description = "Timestamp of the error")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP status code")
    private int status;

    @Schema(description = "HTTP reason phrase")
    private String error;

    @Schema(description = "Human-readable message")
    private String message;

    @Schema(description = "Request path that triggered the error")
    private String path;

    @Schema(description = "Field-level validation errors", nullable = true)
    private Map<String, String> validationErrors;

    public ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path, Map<String, String> validationErrors) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.validationErrors = validationErrors;
    }
}