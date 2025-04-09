package com.commerce.saleday.application.service.discount;

import static org.assertj.core.api.Assertions.assertThat;

import com.commerce.saleday.domain.discount.model.DiscountResult;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class DiscountServiceTest {

  @Autowired
  DiscountService discountService;

  @Test
  void getDiscountResult() {
    //given
    double itemPrice = 23000;

    //when
    DiscountResult discountResult = discountService.getDiscountResult(itemPrice);

    //then
    assertThat(discountResult.getDiscountAmount()).isEqualTo(2000);

  }
}