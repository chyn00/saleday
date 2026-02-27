package com.commerce.saleday.item.service.review;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.commerce.saleday.discount.domain.discount.model.DiscountType;
import com.commerce.saleday.item.domain.item.model.Item;
import com.commerce.saleday.item.domain.review.model.Review;
import com.commerce.saleday.item.domain.review.repository.ReviewRepository;
import com.commerce.saleday.item.service.item.ItemService;
import com.commerce.saleday.item.service.review.model.CreateReviewCommand;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

  @Mock
  private ReviewRepository reviewRepository;

  @Mock
  private ItemService itemService;

  @InjectMocks
  private ReviewService reviewService;

  @Test
  @DisplayName("리뷰 저장 시 생성된 리뷰 id를 반환한다")
  void saveReview_returnsSavedReviewId() {
    Item item = Item.createWithDiscountType(
        "ITEM-1",
        "item",
        "content",
        BigDecimal.valueOf(1000),
        new ArrayList<>(),
        DiscountType.NONE
    );

    Review saved = org.mockito.Mockito.mock(Review.class);
    when(saved.getId()).thenReturn(10L);

    when(itemService.getItem("ITEM-1")).thenReturn(item);
    when(reviewRepository.createItemReview(any(Review.class))).thenReturn(saved);

    Long id = reviewService.saveReview(new CreateReviewCommand("ITEM-1", "USER", 4.0, "good"));

    assertThat(id).isEqualTo(10L);
  }
}
