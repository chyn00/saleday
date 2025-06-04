package com.commerce.saleday.api.presentation.order.controller;

import com.commerce.saleday.api.service.orchestrator.OrderOrchestratorService;
import com.commerce.saleday.order.service.order.OrderService;
import com.commerce.saleday.api.presentation.order.model.OrderRequestDto;
import com.commerce.saleday.api.presentation.order.model.OrderResponseDto;
import com.commerce.saleday.api.presentation.order.model.bulk.BulkOrderRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;
  private final OrderOrchestratorService orderOrchestratorService;

  //단건 주문 저장
  @PostMapping("/order")
  public String orderItem(@Valid @RequestBody OrderRequestDto requestDto) {

    return orderService.saveOrder(requestDto.toCommand());
  }

  //단건 주문 저장(대용량 트래픽 제한된 재고 내에서, 동시성 보장 API)
  @PostMapping("/order/stock/limit")
  public String orderLimitedStock(@Valid @RequestBody OrderRequestDto requestDto) throws Exception {

    return orderOrchestratorService.orderWithLimitedStock(requestDto);
  }

  //다건 주문 저장
  @PostMapping("/orders")
  public String orderItems(@Valid @RequestBody BulkOrderRequestDto bulkOrderRequestDto) {

    return orderService.saveOrders(bulkOrderRequestDto.toCommand());
  }

  //단건 주문 조회
  @GetMapping("/order/{orderCode}")
  public OrderResponseDto getOrder(@PathVariable String orderCode) {

    return OrderResponseDto.toResponse(orderService.getOrder(orderCode));
  }

}
