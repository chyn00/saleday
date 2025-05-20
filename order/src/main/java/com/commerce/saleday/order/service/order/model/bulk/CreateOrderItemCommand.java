package com.commerce.saleday.order.service.order.model.bulk;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateOrderItemCommand(
    @NotBlank(message = "상품 코드는 필수입니다.")
    String itemCode,
    @Positive(message = "수량은 양수여야 합니다.")
    int quantity) {

}
