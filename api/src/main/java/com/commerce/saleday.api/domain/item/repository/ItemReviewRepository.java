package com.commerce.saleday.api.domain.item.repository;

import com.commerce.saleday.api.domain.review.model.Review;
import java.util.List;

public interface ItemReviewRepository {

  List<Review> findReviewsWithItem(String itemCode);
}
