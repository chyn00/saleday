package com.commerce.saleday.order.service.discount;


import com.commerce.saleday.order.service.discount.strategy.calculator.FixedDiscountCalculator;
import com.commerce.saleday.order.service.discount.strategy.selector.DiscountPolicySelector;
import com.commerce.saleday.order.domain.discount.DiscountResult;
import com.commerce.saleday.order.domain.item.model.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscountService {

  private final DiscountPolicySelector discountPolicySelector;

  public DiscountResult getDiscountResult(Item item) {
    return discountPolicySelector.select(item);
  }

}
