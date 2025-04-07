package com.commerce.saleday.domain.discount.calculator;

import org.springframework.stereotype.Component;

@Component
public class FixedDiscountCalculator implements DiscountCalculator {

  @Override
  public double calculate(double price) {
    return price - 1000;
  }

  @Override
  public String getReason() {

    return "상시 할인 1000원 행사";
  }
}
