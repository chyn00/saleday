package com.commerce.saleday.domain.discount.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DiscountResult {

  private double discountAmount;
  private String reason;
}
