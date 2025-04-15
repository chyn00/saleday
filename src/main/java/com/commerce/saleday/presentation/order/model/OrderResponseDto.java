package com.commerce.saleday.presentation.order.model;

import com.commerce.saleday.domain.order.model.Orders;
import com.commerce.saleday.presentation.item.model.ItemResponseDto;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderResponseDto {

  private Long id;

  private String code;

  private LocalDate orderDate;

  private List<OrderItemResponse> orderItemResponse;

  @Builder(access = AccessLevel.PRIVATE)
  public OrderResponseDto(Long id, String code, LocalDate orderDate,
      List<OrderItemResponse> orderItemResponse) {
    this.id = id;
    this.code = code;
    this.orderDate = orderDate;
    this.orderItemResponse = orderItemResponse;
  }

  public static OrderResponseDto toResponse(Orders orders) {
    List<OrderItemResponse> orderItemResponse = orders.getOrderItems()
        .stream()
        .map(orderItem -> OrderItemResponse
            .builder()
            .itemResponse(ItemResponseDto.toResponse(orderItem.getItem()))
            .id(orderItem.getId())
            .quantity(orderItem.getQuantity())
            .discountPrice(orderItem.getDiscountPrice())
            .discountPolicyContent(orderItem.getDiscountPolicyContent())
            .orderPrice(orderItem.getOrderPrice())
            .build()).toList();

    return OrderResponseDto
        .builder()
        .id(orders.getId())
        .code(orders.getCode())
        .orderDate(orders.getOrderDate())
        .orderItemResponse(orderItemResponse)
        .build();
  }
}
