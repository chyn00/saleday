package com.commerce.saleday.presentation.item.model;

import com.commerce.saleday.domain.item.model.Item;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemResponseDto {

  private Long id;// +1로 생성되는 id(고유번호)

  private String code;//상품코드

  private String name;// 이름

  private String content;// 내용

  private double price;// 가격

  public static ItemResponseDto toResponse(Item item){
    return ItemResponseDto
        .builder()
        .id(item.getId())
        .code(item.getCode())
        .name(item.getName())
        .content(item.getContent())
        .price(item.getPrice())
        .build();
  }
}
