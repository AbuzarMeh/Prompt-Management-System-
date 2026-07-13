package com.ats.prompt_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreatePromptRequest {

    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String content;

    private String tags;

    private String modelTarget;
}