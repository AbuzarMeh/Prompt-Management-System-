package com.ats.prompt_service.dto.response;


import java.time.LocalDateTime;
import java.util.UUID;

public class PromptResponse {

    private UUID id;

    private String name;

    private String description;

    private String content;

    private String tags;

    private String modelTarget;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public PromptResponse() {
    }

    public PromptResponse(UUID id, String name, String description, String content, String tags,
                          String modelTarget, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.content = content;
        this.tags = tags;
        this.modelTarget = modelTarget;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
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
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private UUID id;
        private String name;
        private String description;
        private String content;
        private String tags;
        private String modelTarget;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder content(String content) { this.content = content; return this; }
        public Builder tags(String tags) { this.tags = tags; return this; }
        public Builder modelTarget(String modelTarget) { this.modelTarget = modelTarget; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public PromptResponse build() {
            return new PromptResponse(id, name, description, content, tags, modelTarget, createdAt, updatedAt);
        }
    }
}
