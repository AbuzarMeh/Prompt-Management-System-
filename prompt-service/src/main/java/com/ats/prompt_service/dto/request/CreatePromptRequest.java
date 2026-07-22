package com.ats.prompt_service.dto.request;

import com.ats.prompt_service.validation.PromptContentSafety;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public class CreatePromptRequest {

    @NotBlank(message = "Prompt name cannot be empty")
    @Size(max = 100, message = "Prompt name cannot exceed 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotBlank(message = "Prompt content cannot be empty")
    private String content;

    @Size(max = 255, message = "Tags cannot exceed 255 characters")
    private String tags;

    @Size(max = 100, message = "Model target cannot exceed 100 characters")
    private String modelTarget;

    @AssertFalse(message = "Prompt content contains unsafe instruction patterns")
    public boolean isContentUnsafe() {
        return PromptContentSafety.isPotentiallyVulnerable(content);
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public String getModelTarget() { return modelTarget; }
    public void setModelTarget(String modelTarget) { this.modelTarget = modelTarget; }
}
