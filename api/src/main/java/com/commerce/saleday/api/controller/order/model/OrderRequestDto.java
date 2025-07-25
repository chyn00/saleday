package com.commerce.saleday.api.controller.order.model;

import com.commerce.saleday.message.stock.DecreaseStockEvent;
import com.commerce.saleday.order.service.order.model.CreateOrderCommand;
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
public class OrderRequestDto {

  @NotBlank(message = "상품코드는 필수입니다.")
  private String itemCode;//Item 코드(상품 검색을 위한)

  @NotBlank(message = "유저 ID는 필수입니다.")
  private String userId;

  @Positive(message = "상품 수량은 양수값으로 필수입니다.")
  private int quantity; // 주문 수량

  public DecreaseStockEvent toDecreaseStockEvent() {
    return DecreaseStockEvent.toEventMessage(this.itemCode, this.quantity);
  }

  public CreateOrderCommand toCommand() {
    return new CreateOrderCommand(this.itemCode, this.userId, this.quantity);
  }
}
