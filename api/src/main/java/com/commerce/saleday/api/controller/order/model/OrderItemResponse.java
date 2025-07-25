package com.commerce.saleday.api.controller.order.model;

import com.commerce.saleday.api.controller.item.model.ItemResponseDto;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderItemResponse {

  private Long id;// +1로 생성되는 id(고유번호)

  private int quantity; // 주문 수량

  private BigDecimal discountPrice;//할인 금액

  private String discountPolicyContent;//할인 정책 이유

  private BigDecimal orderPrice; // (item에 있는 price) * qty - discountPrice

  private ItemResponseDto itemResponse;//item 결과

}
