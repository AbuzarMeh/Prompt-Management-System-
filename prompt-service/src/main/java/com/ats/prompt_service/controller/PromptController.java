package com.ats.prompt_service.controller;

import com.ats.prompt_service.dto.request.CreatePromptRequest;
import com.ats.prompt_service.dto.request.UpdatePromptRequest;
import com.ats.prompt_service.dto.response.PromptResponse;
import com.ats.prompt_service.entity.Prompt;
import com.ats.prompt_service.service.PromptService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/prompts")
@Tag(name = "Prompts", description = "Operations for managing prompt records")
public class PromptController {

    private final PromptService promptService;

    public PromptController(PromptService promptService) {
        this.promptService = promptService;
    }

    @PostMapping
    @Operation(summary = "Create a prompt", description = "Creates a new prompt record and returns the saved entity.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Prompt created", content = @Content(schema = @Schema(implementation = Prompt.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Prompt> createPrompt(
            @Valid @RequestBody CreatePromptRequest request) {

        Prompt createdPrompt = promptService.createPrompt(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdPrompt);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a prompt by id", description = "Fetches a single prompt using its UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prompt found", content = @Content(schema = @Schema(implementation = PromptResponse.class))),
            @ApiResponse(responseCode = "404", description = "Prompt not found", content = @Content(schema = @Schema(implementation = com.ats.prompt_service.exception.ErrorResponse.class)))
    })
    public ResponseEntity<PromptResponse> getPromptById(
            @Parameter(description = "Prompt UUID", example = "e1ab99b4-c624-475d-acb4-63715a6e7c9e")
            @PathVariable UUID id) {

        Prompt p = promptService.getPromptById(id);

        PromptResponse response = PromptResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .content(p.getContent())
                .tags(p.getTags())
                .modelTarget(p.getModelTarget())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Replace a prompt", description = "Updates an existing prompt with the supplied payload.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prompt updated", content = @Content(schema = @Schema(implementation = Prompt.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content(schema = @Schema(implementation = com.ats.prompt_service.exception.ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Prompt not found", content = @Content(schema = @Schema(implementation = com.ats.prompt_service.exception.ErrorResponse.class)))
    })
    public ResponseEntity<Prompt> updatePrompt(
            @Parameter(description = "Prompt UUID", example = "e1ab99b4-c624-475d-acb4-63715a6e7c9e")
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePromptRequest request) {

        return ResponseEntity.ok(
                promptService.updatePrompt(id, request)
        );
    }
    @PatchMapping("/{id}")
    @Operation(summary = "Partially update a prompt", description = "Applies partial field updates. Invalid field values return a 400 response.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prompt updated", content = @Content(schema = @Schema(implementation = Prompt.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Prompt not found", content = @Content(schema = @Schema(implementation = com.ats.prompt_service.exception.ErrorResponse.class)))
    })
    public ResponseEntity<?> patchPrompt(
            @Parameter(description = "Prompt UUID", example = "e1ab99b4-c624-475d-acb4-63715a6e7c9e")
            @PathVariable UUID id,
            @RequestBody Map<String, Object> updates) {

        return ResponseEntity.ok(promptService.patchPrompt(id, updates));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a prompt", description = "Deletes a prompt by UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Prompt deleted"),
            @ApiResponse(responseCode = "404", description = "Prompt not found", content = @Content(schema = @Schema(implementation = com.ats.prompt_service.exception.ErrorResponse.class)))
    })
    public ResponseEntity<Void> deletePrompt(
            @Parameter(description = "Prompt UUID", example = "e1ab99b4-c624-475d-acb4-63715a6e7c9e")
            @PathVariable UUID id) {

        promptService.deletePrompt(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "List prompts", description = "Returns all prompts or filters by tag and optional limit.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prompt list", content = @Content(schema = @Schema(implementation = Prompt.class)))
    })
    public ResponseEntity<List<Prompt>> getPrompts(
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) Integer limit) {

        if (tag == null && limit == null) {
            return ResponseEntity.ok(promptService.getAllPrompts());
        }

        return ResponseEntity.ok(promptService.getPrompts(tag, limit));
    }

    @GetMapping("/{id}/exists")
    @Operation(summary = "Check prompt existence", description = "Returns whether a prompt exists for the supplied UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Existence check result")
    })
    public ResponseEntity<Map<String, Boolean>> promptExists(@PathVariable UUID id) {

        boolean exists = promptService.promptExists(id);

        return ResponseEntity.ok(
                Collections.singletonMap("exists", exists)
        );
    }

}
