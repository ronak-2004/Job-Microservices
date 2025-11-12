package com.ronak.reviewms.review.impl;

import com.ronak.reviewms.review.Review;
import com.ronak.reviewms.review.ReviewRepository;
import com.ronak.reviewms.review.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<Review> getAllReviews(Long companyId) {
        return reviewRepository.findByCompanyId(companyId);
    }

    @Override
    public boolean addReview(Long companyId, Review review) {
        if(companyId != null && review != null) {
            review.setCompanyId(companyId);
            reviewRepository.save(review);
            return true;
        }
        return false;
    }

    @Override
    public Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElse(null);
    }

    @Override
    public boolean updateReview(Long reviewId, Review updatedReview) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if(review != null){
            if(updatedReview.getCompanyId()!=null)review.setCompanyId(updatedReview.getCompanyId());
            if(updatedReview.getDescription()!=null)review.setDescription(updatedReview.getDescription());
            if(updatedReview.getRating()<=5.00 && updatedReview.getRating()>=0.00)review.setRating(updatedReview.getRating());
            if(updatedReview.getTitle()!=null)review.setTitle(updatedReview.getTitle());
            reviewRepository.save(updatedReview);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteReview(Long reviewId) {
        Review review  = reviewRepository.findById(reviewId).orElse(null);
        if(review != null){
            reviewRepository.delete(review);
            return true;
        }
        return false;
    }
}
