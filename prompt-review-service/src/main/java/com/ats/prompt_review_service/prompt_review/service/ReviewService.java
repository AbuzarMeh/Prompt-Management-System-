package com.ats.prompt_review_service.prompt_review.service;

import com.ats.prompt_review_service.prompt_review.dto.CreateReviewRequest;
import com.ats.prompt_review_service.prompt_review.dto.ReviewSummaryResponse;
import com.ats.prompt_review_service.prompt_review.entity.Review;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ReviewService {

    Review createReview(CreateReviewRequest request) throws IOException;

    List<Review> getAllReviews(UUID promptId) throws IOException;

    Review getReviewById(UUID id) throws IOException;

    ReviewSummaryResponse getSummary(UUID promptId) throws IOException;

}