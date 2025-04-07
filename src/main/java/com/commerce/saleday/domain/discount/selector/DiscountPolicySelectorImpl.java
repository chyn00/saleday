package com.commerce.saleday.domain.discount.selector;

import com.commerce.saleday.domain.discount.calculator.DiscountCalculator;
import com.commerce.saleday.domain.discount.model.DiscountResult;

public class DiscountPolicySelectorImpl implements DiscountPolicySelector {

  @Override
  public DiscountResult select(DiscountCalculator discountCalculator, double price) {

    return DiscountResult
        .builder()
        .price(discountCalculator.calculate(price))
        .reason(discountCalculator.getReason())
        .build();
  }
}
