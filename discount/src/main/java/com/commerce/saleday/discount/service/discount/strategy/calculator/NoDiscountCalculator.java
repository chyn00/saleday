package com.commerce.saleday.discount.service.discount.strategy.calculator;

import com.commerce.saleday.discount.domain.discount.DiscountCommand;
import com.commerce.saleday.discount.domain.discount.model.DiscountType;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class NoDiscountCalculator implements DiscountCalculator {

  @Override
  public BigDecimal applyDiscount(DiscountCommand discountCommand) {
    return BigDecimal.ZERO;
  }

  @Override
  public DiscountType getType() {
    return DiscountType.NONE;
  }

  @Override
  public String getReason() {
    return "할인 없음";
  }
}
