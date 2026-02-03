package com.lincee.controller;

import com.lincee.dto.ReviewDTO;
import com.lincee.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
@Tag(name = "Product Reviews", description = "APIs for managing product reviews and ratings")
public class ReviewController {
    
    @Autowired
    private ReviewService reviewService;
    
    @PostMapping
    @Operation(summary = "Add review", description = "Add a new review for a product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Review created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid review data"),
        @ApiResponse(responseCode = "404", description = "Product or User not found"),
        @ApiResponse(responseCode = "409", description = "User has already reviewed this product")
    })
    public ResponseEntity<ReviewDTO> addReview(
            @Parameter(description = "Product ID") @RequestParam Long productId,
            @Parameter(description = "User ID") @RequestParam Long userId,
            @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO createdReview = reviewService.addReview(productId, userId, reviewDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get review by ID", description = "Retrieve a specific review by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Review found"),
        @ApiResponse(responseCode = "404", description = "Review not found")
    })
    public ResponseEntity<ReviewDTO> getReviewById(
            @Parameter(description = "Review ID") @PathVariable Long id) {
        ReviewDTO review = reviewService.getReviewById(id);
        return ResponseEntity.ok(review);
    }
    
    @GetMapping("/product/{productId}")
    @Operation(summary = "Get product reviews", description = "Retrieve all reviews for a specific product")
    @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully")
    public ResponseEntity<Page<ReviewDTO>> getProductReviews(
            @Parameter(description = "Product ID") @PathVariable Long productId,
            Pageable pageable) {
        Page<ReviewDTO> reviews = reviewService.getProductReviews(productId, pageable);
        return ResponseEntity.ok(reviews);
    }
    
    @GetMapping("/product/{productId}/list")
    @Operation(summary = "Get product reviews list", description = "Retrieve all reviews for a product as a list")
    @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully")
    public ResponseEntity<List<ReviewDTO>> getProductReviewsList(
            @Parameter(description = "Product ID") @PathVariable Long productId) {
        List<ReviewDTO> reviews = reviewService.getProductReviewsList(productId);
        return ResponseEntity.ok(reviews);
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user reviews", description = "Retrieve all reviews written by a specific user")
    @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully")
    public ResponseEntity<Page<ReviewDTO>> getUserReviews(
            @Parameter(description = "User ID") @PathVariable Long userId,
            Pageable pageable) {
        Page<ReviewDTO> reviews = reviewService.getUserReviews(userId, pageable);
        return ResponseEntity.ok(reviews);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update review", description = "Update a specific review")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Review updated successfully"),
        @ApiResponse(responseCode = "404", description = "Review not found")
    })
    public ResponseEntity<ReviewDTO> updateReview(
            @Parameter(description = "Review ID") @PathVariable Long id,
            @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO updatedReview = reviewService.updateReview(id, reviewDTO);
        return ResponseEntity.ok(updatedReview);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete review", description = "Delete a specific review")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Review deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Review not found")
    })
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "Review ID") @PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/helpful")
    @Operation(summary = "Mark review as helpful", description = "Increment helpful count for a review")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Review marked as helpful"),
        @ApiResponse(responseCode = "404", description = "Review not found")
    })
    public ResponseEntity<ReviewDTO> markAsHelpful(
            @Parameter(description = "Review ID") @PathVariable Long id) {
        ReviewDTO review = reviewService.markAsHelpful(id);
        return ResponseEntity.ok(review);
    }
    
    @GetMapping("/product/{productId}/stats")
    @Operation(summary = "Get product rating stats", description = "Get average rating and review count for a product")
    @ApiResponse(responseCode = "200", description = "Stats retrieved successfully")
    public ResponseEntity<Map<String, Object>> getProductRatingStats(
            @Parameter(description = "Product ID") @PathVariable Long productId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("averageRating", reviewService.getProductAverageRating(productId));
        stats.put("reviewCount", reviewService.getProductReviewCount(productId));
        return ResponseEntity.ok(stats);
    }
}
