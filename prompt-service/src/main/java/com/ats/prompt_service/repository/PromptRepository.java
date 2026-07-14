package com.ats.prompt_service.repository;

import com.ats.prompt_service.entity.Prompt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PromptRepository extends JpaRepository<Prompt, UUID> {

    List<Prompt> findByTagsContainingIgnoreCase(String tag);

}