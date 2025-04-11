package com.commerce.saleday.application.service.discount.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.commerce.saleday.application.service.discount.DiscountService;
import com.commerce.saleday.domain.discount.model.DiscountResult;
import com.commerce.saleday.domain.item.model.Item;
import jakarta.transaction.Transactional;
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
  void getDiscountResult(double itemPrice, double expected) {
    //given
    Item item = Item.builder()
        .price(itemPrice)
        .build();

    //when
    DiscountResult discountResult = discountService.getDiscountResult(item);

    //then
    assertThat(discountResult.getDiscountAmount()).isEqualTo(expected);

  }
}