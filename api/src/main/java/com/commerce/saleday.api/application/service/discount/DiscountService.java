package com.commerce.saleday.api.application.service.discount;

import com.commerce.saleday.api.domain.discount.calculator.FixedDiscountCalculator;
import com.commerce.saleday.api.domain.discount.model.DiscountResult;
import com.commerce.saleday.api.domain.discount.selector.DiscountPolicySelector;
import com.commerce.saleday.api.domain.item.model.Item;
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
