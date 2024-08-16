package com.huy2209.library_backend.service;

import com.huy2209.library_backend.dao.BookRepository;
import com.huy2209.library_backend.dao.ReviewRepository;
import com.huy2209.library_backend.dto.ReviewRequest;
import com.huy2209.library_backend.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService implements IReviewService{
    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;


    @Override
    public void postReview(String userEmail, ReviewRequest reviewRequest) throws Exception {
        Review validateReview = reviewRepository.findByUserEmailAndBookId(userEmail,reviewRequest.getBookId());
        if(validateReview != null){
            throw new Exception("Review already existed");
        }
        Review review = Review
                .builder()
                .bookId(reviewRequest.getBookId())
                .rating(reviewRequest.getRating())
                .date(Date.valueOf(LocalDate.now()))
                .userEmail(userEmail)
                .build();

        if(reviewRequest.getReviewDescription() != null){
            review.setReviewDescription(reviewRequest.getReviewDescription());
        }
        else{
            review.setReviewDescription(" ");
        }

        reviewRepository.save(review);
    }

    @Override
    public Boolean userReviewListed(String userEmail, Long bookId) {
        Review validateReview = reviewRepository.findByUserEmailAndBookId(userEmail,bookId);
        return validateReview != null;
    }
}
