package com.commerce.saleday.presentation.order.model;

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

  private double discountPrice;//할인 금액

  private String discountPolicyContent;//할인 정책 이유

  private double orderPrice; // (item에 있는 price) * qty - discountPrice

}
