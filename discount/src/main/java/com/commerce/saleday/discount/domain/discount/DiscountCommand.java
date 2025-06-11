package com.commerce.saleday.discount.domain.discount;

import com.commerce.saleday.discount.domain.discount.model.DiscountType;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DiscountCommand {

  BigDecimal price;
  DiscountType discountType;
}
