package com.commerce.saleday.item.infra.database.repository.review;

import com.commerce.saleday.item.domain.review.model.Review;
import com.commerce.saleday.item.domain.review.repository.ReviewRepository;
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
