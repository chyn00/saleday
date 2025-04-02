package com.commerce.saleday.presentation.order.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderRequestDto {

  @NotBlank(message = "상품코드는 필수입니다.")
  String itemCode;//Item 코드(상품 검색을 위한)

  @NotBlank(message = "유저 ID는 필수입니다.")
  private String userId;

  @NotBlank(message = "상품 수량은 필수입니다.")
  private int quantity; // 주문 수량

}
