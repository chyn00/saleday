package com.commerce.saleday.item.domain.item.repository;

import com.commerce.saleday.item.domain.review.model.Review;
import java.util.List;

public interface ItemReviewRepository {

  List<Review> findReviewsWithItem(String itemCode);
}
