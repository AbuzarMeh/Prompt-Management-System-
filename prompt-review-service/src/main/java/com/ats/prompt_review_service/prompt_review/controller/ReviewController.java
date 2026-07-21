package com.ats.prompt_review_service.prompt_review.controller;

import com.ats.prompt_review_service.prompt_review.dto.request.CreateReviewRequest;
import com.ats.prompt_review_service.prompt_review.dto.response.ReviewSummaryResponse;
import com.ats.prompt_review_service.prompt_review.entity.Review;
import com.ats.prompt_review_service.prompt_review.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<Review> createReview(
            @Valid @RequestBody CreateReviewRequest request) {

        Review review = reviewService.createReview(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    @GetMapping
    public ResponseEntity<List<Review>> getReviews(
            @RequestParam(required = false) UUID promptId) {

        return ResponseEntity.ok(
                reviewService.getAllReviews(promptId)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReview(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                reviewService.getReviewById(id)
        );
    }

    @GetMapping("/{promptId}/summary")
    public ResponseEntity<ReviewSummaryResponse> getSummary(
            @PathVariable UUID promptId) {

        return ResponseEntity.ok(
                reviewService.getReviewSummary(promptId)
        );
    }
}