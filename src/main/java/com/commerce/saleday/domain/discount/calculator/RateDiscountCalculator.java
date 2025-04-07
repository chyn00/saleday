package com.commerce.saleday.domain.discount.calculator;

import org.springframework.stereotype.Component;

@Component
public class RateDiscountCalculator implements DiscountCalculator {

  @Override
  public double calculate(double price) {
    return price - price * 0.1;
  }

  @Override
  public String getReason() {

    return "상시 할인 10% 행사";
  }
}
