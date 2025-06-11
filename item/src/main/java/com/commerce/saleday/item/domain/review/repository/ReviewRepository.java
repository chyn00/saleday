package com.commerce.saleday.item.domain.review.repository;

import com.commerce.saleday.item.domain.review.model.Review;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository {

  Review createItemReview(Review review);
}
