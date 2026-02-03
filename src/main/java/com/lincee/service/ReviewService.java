package com.lincee.service;

import com.lincee.dto.ReviewDTO;
import com.lincee.entity.Product;
import com.lincee.entity.Review;
import com.lincee.entity.User;
import com.lincee.repository.ProductRepository;
import com.lincee.repository.ReviewRepository;
import com.lincee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReviewService {
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public ReviewDTO addReview(Long productId, Long userId, ReviewDTO reviewDTO) {
        Optional<Product> product = productRepository.findById(productId);
        if (!product.isPresent()) {
            throw new RuntimeException("Product not found");
        }
        
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new RuntimeException("User not found");
        }
        
        // Check if user already reviewed this product
        Optional<Review> existingReview = reviewRepository.findByProductIdAndUserId(productId, userId);
        if (existingReview.isPresent()) {
            throw new RuntimeException("User has already reviewed this product");
        }
        
        Review review = new Review();
        review.setProduct(product.get());
        review.setUser(user.get());
        review.setRating(reviewDTO.getRating());
        review.setTitle(reviewDTO.getTitle());
        review.setComment(reviewDTO.getComment());
        review.setVerifiedPurchase(reviewDTO.getVerifiedPurchase() != null ? reviewDTO.getVerifiedPurchase() : false);
        
        Review savedReview = reviewRepository.save(review);
        return convertToDTO(savedReview);
    }
    
    public ReviewDTO getReviewById(Long reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        return review.map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Review not found"));
    }
    
    public Page<ReviewDTO> getProductReviews(Long productId, Pageable pageable) {
        return reviewRepository.findByProductId(productId, pageable).map(this::convertToDTO);
    }
    
    public List<ReviewDTO> getProductReviewsList(Long productId) {
        return reviewRepository.findByProductId(productId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Page<ReviewDTO> getUserReviews(Long userId, Pageable pageable) {
        return reviewRepository.findByUserId(userId, pageable).map(this::convertToDTO);
    }
    
    public ReviewDTO updateReview(Long reviewId, ReviewDTO reviewDTO) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        if (!review.isPresent()) {
            throw new RuntimeException("Review not found");
        }
        
        Review existingReview = review.get();
        if (reviewDTO.getRating() != null) {
            existingReview.setRating(reviewDTO.getRating());
        }
        if (reviewDTO.getTitle() != null) {
            existingReview.setTitle(reviewDTO.getTitle());
        }
        if (reviewDTO.getComment() != null) {
            existingReview.setComment(reviewDTO.getComment());
        }
        
        Review updatedReview = reviewRepository.save(existingReview);
        return convertToDTO(updatedReview);
    }
    
    public void deleteReview(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new RuntimeException("Review not found");
        }
        reviewRepository.deleteById(reviewId);
    }
    
    public Double getProductAverageRating(Long productId) {
        Double averageRating = reviewRepository.getAverageRatingByProductId(productId);
        return averageRating != null ? averageRating : 0.0;
    }
    
    public Long getProductReviewCount(Long productId) {
        return reviewRepository.countByProductId(productId);
    }
    
    public ReviewDTO markAsHelpful(Long reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        if (!review.isPresent()) {
            throw new RuntimeException("Review not found");
        }
        
        Review existingReview = review.get();
        existingReview.setHelpfulCount(existingReview.getHelpfulCount() + 1);
        Review updatedReview = reviewRepository.save(existingReview);
        return convertToDTO(updatedReview);
    }
    
    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setProductId(review.getProduct().getId());
        dto.setUserId(review.getUser().getId());
        dto.setUserName(review.getUser().getUsername());
        dto.setRating(review.getRating());
        dto.setTitle(review.getTitle());
        dto.setComment(review.getComment());
        dto.setHelpfulCount(review.getHelpfulCount());
        dto.setVerifiedPurchase(review.getVerifiedPurchase());
        dto.setCreatedAt(review.getCreatedAt());
        return dto;
    }
}
