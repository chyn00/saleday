package com.commerce.saleday.order.domain.discount;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DiscountResult {

  private BigDecimal discountAmount;
  private String reason;
}
