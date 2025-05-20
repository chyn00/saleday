package com.commerce.saleday.api.presentation.order.model.bulk;

import com.commerce.saleday.order.service.order.model.bulk.CreateOrderItemCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderItemRequest {

  @NotBlank(message = "상품코드는 필수입니다.")
  String itemCode;//Item 코드(상품 검색을 위한)

  @Positive(message = "상품 수량은 양수값으로 필수입니다.")
  private int quantity; // 주문 수량

  public CreateOrderItemCommand toCommand() {
    return new CreateOrderItemCommand(itemCode, quantity);
  }
}
