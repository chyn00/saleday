package com.commerce.saleday.order.service.discount.strategy.selector;

import com.commerce.saleday.order.service.discount.strategy.calculator.DiscountCalculator;
import com.commerce.saleday.order.domain.discount.DiscountResult;
import com.commerce.saleday.order.domain.item.model.Item;
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
