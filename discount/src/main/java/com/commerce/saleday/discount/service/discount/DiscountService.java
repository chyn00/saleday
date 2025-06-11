package com.commerce.saleday.discount.service.discount;


import com.commerce.saleday.discount.domain.discount.DiscountCommand;
import com.commerce.saleday.discount.domain.discount.DiscountResult;
import com.commerce.saleday.discount.service.discount.strategy.selector.DiscountPolicySelector;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscountService {

  private final DiscountPolicySelector discountPolicySelector;

  public DiscountResult getDiscountResult(DiscountCommand discountCommand) {
    return discountPolicySelector.select(discountCommand);
  }

}
