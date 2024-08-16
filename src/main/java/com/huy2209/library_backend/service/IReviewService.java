package com.huy2209.library_backend.service;

import com.huy2209.library_backend.dto.ReviewRequest;

public interface IReviewService {
    void postReview(String userEmail, ReviewRequest reviewRequest) throws Exception;

    Boolean userReviewListed(String userEmail,Long bookId);
}
