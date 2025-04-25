package com.commerce.saleday.api.domain.discount.selector;

import com.commerce.saleday.api.domain.discount.calculator.DiscountCalculator;
import com.commerce.saleday.api.domain.discount.model.DiscountResult;
import com.commerce.saleday.api.domain.item.model.Item;
import org.springframework.stereotype.Component;

@Component
public class DiscountPolicySelectorImpl implements DiscountPolicySelector {

  @Override
  public DiscountResult select(DiscountCalculator discountCalculator, Item item) {

    return DiscountResult
        .builder()
        .discountAmount(discountCalculator.applyDiscount(item))
        .reason(discountCalculator.getReason())
        .build();
  }
}
