package com.commerce.saleday.order.service.discount.strategy.selector;

import com.commerce.saleday.order.domain.discount.model.DiscountType;
import com.commerce.saleday.order.service.discount.strategy.calculator.DiscountCalculator;
import com.commerce.saleday.order.domain.discount.DiscountResult;
import com.commerce.saleday.order.domain.item.model.Item;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiscountPolicySelectorImpl implements DiscountPolicySelector {

  private final Map<DiscountType, DiscountCalculator> discountCalculatorRegistry;

  @Override
  public DiscountResult select(Item item) {

    //Fixed, Rated, None 포함
    DiscountCalculator discountCalculator = discountCalculatorRegistry.get(item.getDiscountType());

    return DiscountResult
        .builder()
        .discountAmount(discountCalculator.applyDiscount(item))
        .reason(discountCalculator.getReason())
        .build();
  }
}
