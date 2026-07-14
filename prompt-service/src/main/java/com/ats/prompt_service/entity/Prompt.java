package com.ats.prompt_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "prompts")
@Schema(name = "Prompt", description = "Prompt stored in the service")
public class Prompt {

    @Id
    @UuidGenerator
    @Schema(description = "Unique prompt identifier", example = "e1ab99b4-c624-475d-acb4-63715a6e7c9e", accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;

    @NotBlank(message = "Prompt name cannot be empty")
    @Size(max = 100, message = "Prompt name cannot exceed 100 characters")
    @Column(nullable = false)
    @Schema(description = "Prompt title", example = "Write a concise project summary")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Schema(description = "Optional prompt description", example = "Used for internal documentation drafts")
    private String description;

    @NotBlank(message = "Prompt content cannot be empty")
    @Column(columnDefinition = "TEXT", nullable = false)
    @Schema(description = "Prompt content", example = "Summarize the project goals in 3 bullets.")
    private String content;

    @Size(max = 255, message = "Tags cannot exceed 255 characters")
    @Schema(description = "Comma-separated tags", example = "writing,summary,internal")
    private String tags;

    @Size(max = 100, message = "Model target cannot exceed 100 characters")
    @Schema(description = "Target model name", example = "gpt-4o-mini")
    private String modelTarget;

    @Schema(description = "Creation timestamp", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;

    public Prompt() {
    }

    public Prompt(UUID id, String name, String description, String content,
                  String tags, String modelTarget,
                  LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.content = content;
        this.tags = tags;
        this.modelTarget = modelTarget;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getModelTarget() {
        return modelTarget;
    }

    public void setModelTarget(String modelTarget) {
        this.modelTarget = modelTarget;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}