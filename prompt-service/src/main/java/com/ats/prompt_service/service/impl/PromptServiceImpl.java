package com.ats.prompt_service.service.impl;

import com.ats.prompt_service.exception.PromptNotFoundException;
import com.ats.prompt_service.entity.Prompt;
import com.ats.prompt_service.repository.PromptRepository;
import com.ats.prompt_service.service.PromptService;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.function.Consumer;
import org.springframework.stereotype.Service;
import jakarta.validation.ConstraintViolation;

import jakarta.validation.Validator;
import com.ats.prompt_service.exception.ValidationException;


@Service
public class PromptServiceImpl implements PromptService {

    private final PromptRepository promptRepository;
    private final Validator validator;

    // Constructor Injection
    public PromptServiceImpl(PromptRepository promptRepository,
                         Validator validator) {

        this.promptRepository = promptRepository;
        this.validator = validator;
    }

    @Override
    public Prompt createPrompt(Prompt prompt) {
        return promptRepository.save(prompt);
    }

    // @Override
    // public List<Prompt> getAllPrompts() {
    //     return promptRepository.findAll();
    // }
    @Override
    public List<Prompt> getAllPrompts() {

        List<Prompt> prompts = promptRepository.findAll();

        System.out.println("Total prompts = " + prompts.size());

        prompts.forEach(System.out::println);

        return prompts;
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

    @Override
    public boolean promptExists(UUID id) {
        return promptRepository.existsById(id);
    }

    @Override
    public Prompt patchPrompt(UUID id, Map<String, Object> updates) {

        Prompt prompt = promptRepository.findById(id)
                .orElseThrow(() ->
                        new PromptNotFoundException("Prompt not found with id: " + id));

        applyStringUpdate(updates, "name", prompt::setName);
        applyStringUpdate(updates, "description", prompt::setDescription);
        applyStringUpdate(updates, "content", prompt::setContent);
        applyStringUpdate(updates, "tags", prompt::setTags);
        applyStringUpdate(updates, "modelTarget", prompt::setModelTarget);

        var violations = validator.validate(prompt);

if (!violations.isEmpty()) {

    Map<String, String> errors = new java.util.HashMap<>();

        for (ConstraintViolation<Prompt> violation : violations) {
            errors.put(
                    violation.getPropertyPath().toString(),
                    violation.getMessage()
            );
        }

        throw new ValidationException(errors);
    }

        return promptRepository.save(prompt);
    }

    private void applyStringUpdate(
            Map<String, Object> updates,
            String field,
            Consumer<String> setter) {

        if (!updates.containsKey(field)) {
            return;
        }

        Object value = updates.get(field);

        if (value != null && !(value instanceof String)) {
            throw new ValidationException(
                    Map.of(field, field + " must be a string")
            );
        }

        setter.accept((String) value);
    }
}
