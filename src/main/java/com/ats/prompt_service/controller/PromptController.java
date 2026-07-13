package com.ats.prompt_service.controller;

import com.ats.prompt_service.entity.Prompt;
import com.ats.prompt_service.service.PromptService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/prompts")
public class PromptController {

    private final PromptService promptService;

    public PromptController(PromptService promptService) {
        this.promptService = promptService;
    }

    @PostMapping
    public ResponseEntity<Prompt> createPrompt(
            @Valid @RequestBody Prompt prompt) {

        Prompt createdPrompt = promptService.createPrompt(prompt);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdPrompt);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prompt> getPromptById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                promptService.getPromptById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Prompt> updatePrompt(
            @PathVariable UUID id,
            @Valid @RequestBody Prompt prompt) {

        return ResponseEntity.ok(
                promptService.updatePrompt(id, prompt)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrompt(
            @PathVariable UUID id) {

        promptService.deletePrompt(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Prompt>> getPrompts(
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) Integer limit) {

        if (tag == null && limit == null) {
            return ResponseEntity.ok(promptService.getAllPrompts());
        }

        return ResponseEntity.ok(promptService.getPrompts(tag, limit));
    }

}