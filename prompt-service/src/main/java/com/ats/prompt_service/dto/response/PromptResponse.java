package com.ats.prompt_service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PromptResponse {

    private UUID id;

    private String name;

    private String description;

    private String content;

    private String tags;

    private String modelTarget;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}