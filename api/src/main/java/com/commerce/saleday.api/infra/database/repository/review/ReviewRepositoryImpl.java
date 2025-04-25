package com.commerce.saleday.api.infra.database.repository.review;

import com.commerce.saleday.api.domain.review.model.Review;
import com.commerce.saleday.api.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {

  private final ReviewJpaRepository reviewJpaRepository;

  @Override
  public Review createItemReview(Review review) {
    return reviewJpaRepository.save(review);
  }
}
