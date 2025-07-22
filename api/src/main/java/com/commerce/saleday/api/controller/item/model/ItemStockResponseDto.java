package com.commerce.saleday.api.controller.item.model;

import com.commerce.saleday.item.domain.item.model.Item;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemStockResponseDto {
  private String code;//상품코드
  private String quantity;//상품 개수

  public static ItemStockResponseDto toResponse(String code, String quantity){
    return ItemStockResponseDto
        .builder()
        .code(code)
        .quantity(quantity)
        .build();
  }
}
