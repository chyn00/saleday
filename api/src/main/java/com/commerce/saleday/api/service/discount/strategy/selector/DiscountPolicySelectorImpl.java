package com.commerce.saleday.api.service.discount.strategy.selector;

import com.commerce.saleday.api.service.discount.strategy.calculator.DiscountCalculator;
import com.commerce.saleday.domain.discount.model.DiscountResult;
import com.commerce.saleday.domain.item.model.Item;
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
