package com.commerce.saleday.order.service.item;

import static org.assertj.core.api.Assertions.assertThat;

import com.commerce.saleday.order.service.ItemService;
import com.commerce.saleday.order.service.review.ReviewService;
import com.commerce.saleday.order.domain.item.model.Item;
import com.commerce.saleday.order.domain.review.model.Review;
import com.commerce.saleday.order.service.review.model.CreateReviewCommand;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
    CreateReviewCommand command =
        new CreateReviewCommand(itemCode, "SYSTEM", 4.5, "아주 훌륭한 아이템입니다.");

    // 저장
    reviewService.saveReview(command);
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


  //페이징 테스트 구현
  //PageRequest.of of(pageNumber, pageSize, Sort.unsorted());
  @Test
  void getItemsByCodeContaining() {
    //given
    String itemCode = "123";

    int page = 0; //첫번째 페이지
    int size = 10; //한 페이지에 10개
    Pageable pageable = PageRequest.of(page,size);

    //when
    Page<Item> items = itemService.getItemsByCodeContaining(itemCode, pageable);

    //then
    assertThat(items).size().isEqualTo(3);
  }
}