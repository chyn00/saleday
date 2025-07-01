package com.commerce.saleday.api.controller.item.model;

import com.commerce.saleday.item.domain.item.model.Item;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemResponseDto {

  private Long id;// +1로 생성되는 id(고유번호)

  private String code;//상품코드

  private String name;// 이름

  private String content;// 내용

  private BigDecimal price;// 가격

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

  public static Page<ItemResponseDto> toResponsePage(Page<Item> items) {
    return items.map(ItemResponseDto::toResponse);
  }
}
