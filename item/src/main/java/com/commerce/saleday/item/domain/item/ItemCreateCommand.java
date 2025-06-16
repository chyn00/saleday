package com.commerce.saleday.item.domain.item;

import com.commerce.saleday.discount.domain.discount.model.DiscountType;
import com.commerce.saleday.item.domain.review.model.Review;
import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemCreateCommand {

  private String code;
  private String name;
  private String content;
  private BigDecimal price;
  private DiscountType discountType;
  private List<Review> reviews;

  @Builder
  public ItemCreateCommand(String code, String name, String content, BigDecimal price, DiscountType discountType, List<Review> reviews) {
    this.code = code;
    this.name = name;
    this.content = content;
    this.price = price;
    this.discountType = discountType;
    this.reviews = reviews;
  }
}