package com.commerce.saleday.application.service.item;

import com.commerce.saleday.application.service.review.ReviewService;
import com.commerce.saleday.domain.item.model.Item;
import com.commerce.saleday.domain.review.model.Review;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ItemServiceTest {

  @Autowired
  ItemService itemService;

  @Autowired
  ReviewService reviewService;

  @BeforeEach
  void reviewSetUp() {
    //given
    String itemCode = "1234";
    Review review = Review.builder()
        .userId("SYSTEM")
        .score(4.5)
        .content("아주 훌륭한 아이템입니다.")
        .build();

    // 저장
    reviewService.saveReview(itemCode, review);
  }


  @Test
  void getItem() {
    //given
    String itemCode = "1234";

    //when
    Item item = itemService.getItem(itemCode);

    //then
    assertThat(item).isNotNull();
  }

  @Test
  void getItemReviews() {
    //given
    String itemCode = "1234";

    //when
    List<Review> reviews = itemService.getItemReviews(itemCode);

    //then
    // 초기 단계이기 때문에 추후 수정 필요
    assertThat(reviews).isNotEmpty();
  }
}