package com.commerce.saleday.presentation.order.controller;

import com.commerce.saleday.application.service.order.OrderService;
import com.commerce.saleday.domain.order.model.OrderItem;
import com.commerce.saleday.presentation.order.model.OrderRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  //단건 주문 저장
  @PostMapping("/orders")
  public String orderItem(@RequestBody OrderRequestDto requestDto) {

    return orderService.saveOrder(requestDto);
  }

}
