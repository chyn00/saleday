package com.commerce.saleday.api.service.review;

import static org.assertj.core.api.Assertions.assertThat;

import com.commerce.saleday.api.presentation.review.model.ReviewRequestDto;
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
    ReviewRequestDto dto = ReviewRequestDto.builder()
        .itemCode(itemCode)
        .userId("SYSTEM")
        .score(4.5)
        .content("아주 훌륭한 아이템입니다.")
        .build();

    //when
    Long id = reviewService.saveReview(dto);

    //then
    assertThat(id).isGreaterThan(0);
  }
}