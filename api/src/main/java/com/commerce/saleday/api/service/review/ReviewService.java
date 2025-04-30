package com.commerce.saleday.api.service.review;

import com.commerce.saleday.api.service.item.ItemService;
import com.commerce.saleday.domain.item.model.Item;
import com.commerce.saleday.domain.review.model.Review;
import com.commerce.saleday.domain.review.repository.ReviewRepository;
import com.commerce.saleday.api.presentation.review.model.ReviewRequestDto;
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
  public Long saveReview(ReviewRequestDto dto) {
    Item item = itemService.getItem(dto.getItemCode());
    Review review = Review
        .create(dto.getUserId(), dto.getScore(), dto.getContent(), item);

    return reviewRepository.createItemReview(review).getId();
  }
}
