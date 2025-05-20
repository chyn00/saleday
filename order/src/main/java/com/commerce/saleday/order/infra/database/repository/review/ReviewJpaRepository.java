package com.commerce.saleday.order.infra.database.repository.review;

import com.commerce.saleday.order.domain.review.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewJpaRepository extends JpaRepository<Review, Long> {

}
