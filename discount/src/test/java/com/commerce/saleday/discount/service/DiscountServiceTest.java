package com.commerce.saleday.discount.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.commerce.saleday.discount.TestDiscountApplication;
import com.commerce.saleday.discount.domain.discount.DiscountCommand;
import com.commerce.saleday.discount.domain.discount.DiscountResult;
import com.commerce.saleday.discount.domain.discount.model.DiscountType;
import com.commerce.saleday.discount.service.discount.DiscountService;
import java.math.BigDecimal;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestDiscountApplication.class)
class DiscountServiceTest {

  @Autowired
  DiscountService discountService;

  @ParameterizedTest
  @CsvSource({"10000,2000", "30000,5000", "50000,8000"})
  void getDiscountResult(BigDecimal itemPrice, BigDecimal expected) {
    //given
    DiscountCommand discountCommand = DiscountCommand
        .builder()
        .price(itemPrice)
        .discountType(DiscountType.FIXED)
        .build();


    //when
    DiscountResult discountResult = discountService.getDiscountResult(discountCommand);

    //then
    assertThat(discountResult.getDiscountAmount()).isEqualTo(expected);

  }
}