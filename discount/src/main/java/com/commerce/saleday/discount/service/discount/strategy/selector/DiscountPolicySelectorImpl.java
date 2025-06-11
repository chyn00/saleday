package com.commerce.saleday.discount.service.discount.strategy.selector;

import com.commerce.saleday.discount.domain.discount.DiscountCommand;
import com.commerce.saleday.discount.domain.discount.DiscountResult;
import com.commerce.saleday.discount.domain.discount.model.DiscountType;
import com.commerce.saleday.discount.service.discount.strategy.calculator.DiscountCalculator;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiscountPolicySelectorImpl implements DiscountPolicySelector {

  //Map을 빈으로 활용해서, 설정된 값을 한 곳에서 관리할 수 있도록 변경
  private final Map<DiscountType, DiscountCalculator> discountCalculatorRegistry;

  @Override
  public DiscountResult select(DiscountCommand discountCommand) {

    //Fixed, Rated, None 포함
    DiscountCalculator discountCalculator = discountCalculatorRegistry.get(discountCommand.getDiscountType());

    return DiscountResult
        .builder()
        .discountAmount(discountCalculator.applyDiscount(discountCommand))
        .reason(discountCalculator.getReason())
        .build();
  }
}
