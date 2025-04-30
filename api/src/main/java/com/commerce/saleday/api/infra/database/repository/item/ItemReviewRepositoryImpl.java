package com.commerce.saleday.api.infra.database.repository.item;

import com.commerce.saleday.domain.item.model.Item;
import com.commerce.saleday.domain.item.model.QItem;
import com.commerce.saleday.domain.item.repository.ItemReviewRepository;
import com.commerce.saleday.domain.review.model.QReview;
import com.commerce.saleday.domain.review.model.Review;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ItemReviewRepositoryImpl implements ItemReviewRepository {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<Review> findReviewsWithItem(String itemCode) {
    QItem item = QItem.item;
    QReview review = QReview.review;

    return Optional.ofNullable(
            jpaQueryFactory
                .selectFrom(item)
                .join(item.reviews, review)
                .fetchJoin()
                .where(item.code.eq(itemCode))
                .fetchOne()
        )
        .map(Item::getReviews)
        .orElseThrow(() -> new EntityNotFoundException("상품 정보가 없습니다."));
  }
}
