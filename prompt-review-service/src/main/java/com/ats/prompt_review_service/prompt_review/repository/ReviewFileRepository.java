package com.ats.prompt_review_service.prompt_review.repository;

import com.ats.prompt_review_service.prompt_review.entity.Review;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ReviewFileRepository {

    private final Path storagePath;
    private final ObjectMapper objectMapper;

    public ReviewFileRepository(
            @Value("${review.storage.path}") String storageDirectory) throws IOException {

        this.storagePath = Paths.get(storageDirectory);

        Files.createDirectories(storagePath);

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public Review save(Review review) {

        try {
            Path file = storagePath.resolve(review.getId() + ".json");

            objectMapper.writeValue(file.toFile(), review);

            return review;

        } catch (IOException ex) {
            throw new RuntimeException("Failed to save review.", ex);
        }
    }

    public Optional<Review> findById(UUID id) {

        try {
            Path file = storagePath.resolve(id + ".json");

            if (!Files.exists(file)) {
                return Optional.empty();
            }

            Review review = objectMapper.readValue(file.toFile(), Review.class);

            return Optional.of(review);

        } catch (IOException ex) {
            throw new RuntimeException("Failed to read review.", ex);
        }
    }

    public List<Review> findAll() {

        try {

            List<Review> reviews = new ArrayList<>();

            DirectoryStream<Path> files =
                    Files.newDirectoryStream(storagePath, "*.json");

            for (Path file : files) {

                reviews.add(
                        objectMapper.readValue(file.toFile(), Review.class)
                );
            }

            return reviews;

        } catch (IOException ex) {
            throw new RuntimeException("Failed to load reviews.", ex);
        }
    }

    public List<Review> findByPromptId(UUID promptId) {

        List<Review> filtered = new ArrayList<>();

        for (Review review : findAll()) {

            if (review.getPromptId().equals(promptId)) {
                filtered.add(review);
            }

        }

        return filtered;
    }

}