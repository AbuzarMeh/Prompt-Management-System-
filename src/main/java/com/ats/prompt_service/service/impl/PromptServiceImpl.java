package com.ats.prompt_service.service.impl;

import com.ats.prompt_service.exception.PromptNotFoundException;
import com.ats.prompt_service.entity.Prompt;
import com.ats.prompt_service.repository.PromptRepository;
import com.ats.prompt_service.service.PromptService;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class PromptServiceImpl implements PromptService {

    private final PromptRepository promptRepository;

    // Constructor Injection
    public PromptServiceImpl(PromptRepository promptRepository) {
        this.promptRepository = promptRepository;
    }

    @Override
    public Prompt createPrompt(Prompt prompt) {
        return promptRepository.save(prompt);
    }

    @Override
    public List<Prompt> getAllPrompts() {
        return promptRepository.findAll();
    }

    @Override
    public List<Prompt> getPrompts(String tag, Integer limit) {

        List<Prompt> prompts;

        if (tag != null && !tag.isBlank()) {
            prompts = promptRepository.findByTagsContainingIgnoreCase(tag);
        } else {
            prompts = getAllPrompts();   // Reuse the existing method
        }

        if (limit != null && limit > 0 && limit < prompts.size()) {
            prompts = prompts.subList(0, limit);
        }

        return prompts;
    }

    @Override
    public Prompt getPromptById(UUID id) {

        return promptRepository.findById(id)
                .orElseThrow(() ->
                        new PromptNotFoundException(
                                "Prompt not found with id: " + id
                        ));
    }

    @Override
    public Prompt updatePrompt(UUID id, Prompt updatedPrompt) {

        Prompt existingPrompt = promptRepository.findById(id)
        .orElseThrow(() ->
                new PromptNotFoundException(
                        "Prompt not found with id: " + id
                ));

        existingPrompt.setName(updatedPrompt.getName());
        existingPrompt.setDescription(updatedPrompt.getDescription());
        existingPrompt.setContent(updatedPrompt.getContent());
        existingPrompt.setTags(updatedPrompt.getTags());
        existingPrompt.setModelTarget(updatedPrompt.getModelTarget());

        return promptRepository.save(existingPrompt);
    }

    @Override
    public void deletePrompt(UUID id) {

        if (!promptRepository.existsById(id)) {
            throw new PromptNotFoundException(
                    "Prompt not found with id: " + id
            );
        }

        promptRepository.deleteById(id);
    }
}