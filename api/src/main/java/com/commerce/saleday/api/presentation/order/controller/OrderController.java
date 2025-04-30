package com.commerce.saleday.api.presentation.order.controller;

import com.commerce.saleday.api.service.order.OrderService;
import com.commerce.saleday.api.presentation.order.model.OrderRequestDto;
import com.commerce.saleday.api.presentation.order.model.OrderResponseDto;
import com.commerce.saleday.api.presentation.order.model.bulk.BulkOrderRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  //단건 주문 저장
  @PostMapping("/order")
  public String orderItem(@Valid @RequestBody OrderRequestDto requestDto) {

    return orderService.saveOrder(requestDto);
  }

  //다건 주문 저장
  @PostMapping("/orders")
  public String orderItems(@Valid @RequestBody BulkOrderRequestDto bulkOrderRequestDto) {

    return orderService.saveOrders(bulkOrderRequestDto);
  }

  //단건 주문 조회
  @GetMapping("/order")
  public OrderResponseDto getOrder(@RequestParam String orderCode) {

    return OrderResponseDto.toResponse(orderService.getOrder(orderCode));
  }

}
