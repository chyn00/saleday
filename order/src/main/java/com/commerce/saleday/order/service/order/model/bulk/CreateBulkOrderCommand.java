package com.commerce.saleday.order.service.order.model.bulk;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record CreateBulkOrderCommand(
    @NotBlank(message = "유저 ID는 필수입니다.")
    String userId,

    @Valid
    @NotEmpty(message = "주문 항목은 1건 이상이어야 합니다.")
    List<CreateOrderItemCommand> orderItemCommandList
) {

}
