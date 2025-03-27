package com.commerce.saleday.domain.item.repository;

import com.commerce.saleday.domain.review.model.Review;

import java.util.List;

public interface ItemReviewRepository {
    List<Review> findReviewsWithItem(String itemCode);
}
