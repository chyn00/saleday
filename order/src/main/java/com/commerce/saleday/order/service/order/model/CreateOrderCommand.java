package com.commerce.saleday.order.service.order.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateOrderCommand(
    @NotBlank(message = "상품코드는 필수입니다.")
    String itemCode,
    @NotBlank(message = "유저 ID는 필수입니다.")
    String userId,
    @Positive(message = "상품 수량은 양수값으로 필수입니다.")
    int quantity) {

}
