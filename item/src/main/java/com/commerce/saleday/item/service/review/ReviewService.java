package com.commerce.saleday.item.service.review;

import com.commerce.saleday.item.domain.item.model.Item;
import com.commerce.saleday.item.domain.review.model.Review;
import com.commerce.saleday.item.domain.review.repository.ReviewRepository;
import com.commerce.saleday.item.service.item.ItemService;
import com.commerce.saleday.item.service.review.model.CreateReviewCommand;
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
