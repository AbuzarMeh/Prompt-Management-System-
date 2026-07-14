package com.ats.prompt_review_service.prompt_review.service.impl;

import com.ats.prompt_review_service.prompt_review.client.PromptServiceClient;
import com.ats.prompt_review_service.prompt_review.dto.CreateReviewRequest;
import com.ats.prompt_review_service.prompt_review.dto.PromptResponse;
import com.ats.prompt_review_service.prompt_review.dto.ReviewSummaryResponse;
import com.ats.prompt_review_service.prompt_review.entity.Review;
import com.ats.prompt_review_service.prompt_review.exception.ReviewNotFoundException;
import com.ats.prompt_review_service.prompt_review.repository.ReviewFileRepository;
import com.ats.prompt_review_service.prompt_review.service.ReviewService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewFileRepository reviewRepository;
    private final PromptServiceClient promptServiceClient;

    public ReviewServiceImpl(
            ReviewFileRepository reviewRepository,
            PromptServiceClient promptServiceClient) {

        this.reviewRepository = reviewRepository;
        this.promptServiceClient = promptServiceClient;
    }

    @Override
    public Review createReview(CreateReviewRequest request) throws IOException {

        PromptResponse prompt =
                promptServiceClient.getPrompt(request.getPromptId());

        Review review = new Review();

        review.initialize();

        review.setPromptId(request.getPromptId());
        review.setPromptSnapshot(prompt.getContent());
        review.setReviewerName(request.getReviewerName());
        review.setScore(request.getScore());
        review.setFeedback(request.getFeedback());

        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getAllReviews(UUID promptId) throws IOException {

        if (promptId == null) {
            return reviewRepository.findAll();
        }

        return reviewRepository.findByPromptId(promptId);
    }

    @Override
    public Review getReviewById(UUID id) throws IOException {

        return reviewRepository.findById(id)
                .orElseThrow(() ->
                        new ReviewNotFoundException(
                                "Review not found with id: " + id
                        ));
    }

    @Override
    public ReviewSummaryResponse getSummary(UUID promptId)
            throws IOException {

        List<Review> reviews =
                reviewRepository.findByPromptId(promptId);

        ReviewSummaryResponse summary =
                new ReviewSummaryResponse();

        summary.setPromptId(promptId);

        summary.setTotalReviews(reviews.size());

        double average =
                reviews.stream()
                        .mapToInt(Review::getScore)
                        .average()
                        .orElse(0);

        summary.setAverageScore(average);

        summary.setFeedback(
                reviews.stream()
                        .map(Review::getFeedback)
                        .collect(Collectors.toList())
        );

        return summary;
    }

}