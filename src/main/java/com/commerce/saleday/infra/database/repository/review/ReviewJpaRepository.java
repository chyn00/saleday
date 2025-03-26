package com.commerce.saleday.infra.database.repository.review;

import com.commerce.saleday.domain.item.model.Item;
import com.commerce.saleday.domain.review.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewJpaRepository extends JpaRepository<Review,Long> {
    Review createReview(Review review);
}
