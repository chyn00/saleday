package com.commerce.saleday.order.service.review;

import static org.assertj.core.api.Assertions.assertThat;

import com.commerce.saleday.order.service.review.model.CreateReviewCommand;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class ReviewServiceTest {

  @Autowired
  ReviewService reviewService;

  @Test
  void saveReview() {
    //given
    String itemCode = "1234";

    CreateReviewCommand command = new CreateReviewCommand(itemCode, "SYSTEM",4.5,"아주 훌륭한 아이템입니다.");
    //when
    Long id = reviewService.saveReview(command);

    //then
    assertThat(id).isGreaterThan(0);
  }
}