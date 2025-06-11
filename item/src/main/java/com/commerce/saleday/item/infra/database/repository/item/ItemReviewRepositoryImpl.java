package com.commerce.saleday.item.infra.database.repository.item;

import com.commerce.saleday.item.domain.item.model.Item;
import com.commerce.saleday.item.domain.item.model.QItem;
import com.commerce.saleday.item.domain.item.repository.ItemReviewRepository;
import com.commerce.saleday.item.domain.review.model.Review;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ItemReviewRepositoryImpl implements ItemReviewRepository {

  private final JPAQueryFactory jpaQueryFactory;

  public ItemReviewRepositoryImpl(
      JPAQueryFactory jpaQueryFactory) {
    this.jpaQueryFactory = jpaQueryFactory;
  }

  @Override
  public List<Review> findReviewsWithItem(String itemCode) {
    QItem item = QItem.item;
//    QReview review = QReview.review;

    return Optional.ofNullable(
            jpaQueryFactory
                .selectFrom(item)
                .join(item.reviews)
                .fetchJoin()
                .where(item.code.eq(itemCode))
                .fetchOne()
        )
        .map(Item::getReviews)
        .orElseThrow(() -> new EntityNotFoundException("상품 정보가 없습니다."));
  }
}
