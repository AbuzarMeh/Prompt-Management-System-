package com.ats.prompt_service.service;

import com.ats.prompt_service.entity.Prompt;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Map;

public interface PromptService {

    Prompt createPrompt(Prompt prompt);

    List<Prompt> getAllPrompts();

    List<Prompt> getPrompts(String tag, Integer limit);
    
    Prompt getPromptById(UUID id);

    Prompt updatePrompt(UUID id, Prompt prompt);

    void deletePrompt(UUID id);

    boolean promptExists(UUID id);
    
    Prompt patchPrompt(UUID id, Map<String, Object> updates);
}