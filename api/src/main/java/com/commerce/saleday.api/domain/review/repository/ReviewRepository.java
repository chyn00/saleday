package com.commerce.saleday.api.domain.review.repository;

import com.commerce.saleday.api.domain.review.model.Review;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository {

  Review createItemReview(Review review);
}
