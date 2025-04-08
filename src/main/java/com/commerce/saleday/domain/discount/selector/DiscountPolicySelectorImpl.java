package com.commerce.saleday.domain.discount.selector;

import com.commerce.saleday.domain.discount.calculator.DiscountCalculator;
import com.commerce.saleday.domain.discount.model.DiscountResult;
import org.springframework.stereotype.Component;

@Component
public class DiscountPolicySelectorImpl implements DiscountPolicySelector {

  @Override
  public DiscountResult select(DiscountCalculator discountCalculator, double price) {

    return DiscountResult
        .builder()
        .discountedPrice(price - discountCalculator.applyDiscount(price))
        .reason(discountCalculator.getReason())
        .build();
  }
}
