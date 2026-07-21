package com.ats.prompt_review_service.prompt_review.service;

import com.ats.prompt_review_service.prompt_review.dto.request.CreateReviewRequest;
import com.ats.prompt_review_service.prompt_review.dto.response.ReviewSummaryResponse;
import com.ats.prompt_review_service.prompt_review.entity.Review;

import java.util.List;
import java.util.UUID;

public interface ReviewService {

    Review createReview(CreateReviewRequest request);

    List<Review> getAllReviews(UUID promptId);

    Review getReviewById(UUID id);

    ReviewSummaryResponse getReviewSummary(UUID promptId);
}