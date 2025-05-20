package com.commerce.saleday.order.service.review;

import com.commerce.saleday.order.service.ItemService;
import com.commerce.saleday.order.domain.item.model.Item;
import com.commerce.saleday.order.domain.review.model.Review;
import com.commerce.saleday.order.domain.review.repository.ReviewRepository;
import com.commerce.saleday.order.service.review.model.CreateReviewCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

  private final ReviewRepository reviewRepository;
  private final ItemService itemService;

  //리뷰를 저장한다.
  @Transactional
  public Long saveReview(CreateReviewCommand command) {
    Item item = itemService.getItem(command.itemCode());
    Review review = Review
        .create(command.userId(), command.score(), command.content(), item);

    return reviewRepository.createItemReview(review).getId();
  }
}
