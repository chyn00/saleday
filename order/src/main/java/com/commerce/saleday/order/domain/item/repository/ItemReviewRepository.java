package com.commerce.saleday.order.domain.item.repository;

import com.commerce.saleday.order.domain.review.model.Review;
import java.util.List;

public interface ItemReviewRepository {

  List<Review> findReviewsWithItem(String itemCode);
}
