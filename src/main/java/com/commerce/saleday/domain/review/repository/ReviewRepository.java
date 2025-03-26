package com.commerce.saleday.domain.review.repository;

import com.commerce.saleday.domain.item.model.Item;
import com.commerce.saleday.domain.review.model.Review;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository {
    Review createItemReview(Review review);
}
