package com.commerce.saleday.item.review;

import static org.assertj.core.api.Assertions.assertThat;

import com.commerce.saleday.item.TestItemApplication;
import com.commerce.saleday.item.service.review.ReviewService;
import com.commerce.saleday.item.service.review.model.CreateReviewCommand;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = TestItemApplication.class)
@ActiveProfiles("test")  // application-test.yml 사용
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