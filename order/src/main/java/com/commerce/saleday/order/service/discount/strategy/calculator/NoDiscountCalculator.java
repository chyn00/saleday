package com.commerce.saleday.order.service.discount.strategy.calculator;

import com.commerce.saleday.order.domain.discount.model.DiscountType;
import com.commerce.saleday.order.domain.item.model.Item;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class NoDiscountCalculator implements DiscountCalculator {

  @Override
  public BigDecimal applyDiscount(Item item) {
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
