package com.commerce.saleday.api.service.discount;


import com.commerce.saleday.api.service.discount.strategy.calculator.FixedDiscountCalculator;
import com.commerce.saleday.api.service.discount.strategy.selector.DiscountPolicySelector;
import com.commerce.saleday.domain.discount.model.DiscountResult;
import com.commerce.saleday.domain.item.model.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscountService {

  private final DiscountPolicySelector discountPolicySelector;
  private final FixedDiscountCalculator fixedDiscountCalculator;

  public DiscountResult getDiscountResult(Item item) {
    return discountPolicySelector.select(fixedDiscountCalculator, item);
  }

}
