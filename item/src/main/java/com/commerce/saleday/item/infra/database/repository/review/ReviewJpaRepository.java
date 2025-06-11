package com.commerce.saleday.item.infra.database.repository.review;

import com.commerce.saleday.item.domain.review.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewJpaRepository extends JpaRepository<Review, Long> {

}
