package com.commerce.saleday.order.service.discount.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.commerce.saleday.order.service.discount.DiscountService;
import com.commerce.saleday.order.domain.discount.DiscountResult;
import com.commerce.saleday.order.domain.item.model.Item;
import com.commerce.saleday.order.domain.review.model.Review;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class DiscountServiceTest {

  @Autowired
  DiscountService discountService;

  @ParameterizedTest
  @CsvSource({"10000,2000", "30000,5000", "50000,8000"})
  void getDiscountResult(BigDecimal itemPrice, BigDecimal expected) {
    //given
    String code = "test-1234";
    String name = "test-과자";
    String content = "test-달달한과자";
    List<Review> reviews = new ArrayList<>();

    Item item = Item.create(code, name, content, itemPrice, reviews);

    //when
    DiscountResult discountResult = discountService.getDiscountResult(item);

    //then
    assertThat(discountResult.getDiscountAmount()).isEqualTo(expected);

  }
}