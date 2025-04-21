package com.commerce.saleday.application.service.orchestrator;

import com.commerce.saleday.application.service.order.OrderService;
import com.commerce.saleday.domain.stock.service.ItemStockDomainService;
import com.commerce.saleday.presentation.order.model.OrderRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// 고트래픽 대응을 위한 재고 수량 감소, 트랜잭션 등을 처리하기 위한 Order 퍼사드 서비스
@Service
@RequiredArgsConstructor
public class OrderOrchestratorService {

  private final ItemStockDomainService itemStockDomainService;
  private final OrderService orderService;

  //고트래픽 대용으로 제한된 재고에서 동시성제어로 주문하며, 주문 코드를 리턴한다.
  //레디스의 트랜잭션은 보상트랜잭션을 활용한다.
  public String orderWithLimitedStock(OrderRequestDto orderRequestDto) throws Exception {


    long remaining = itemStockDomainService.decrementAndCountItemStock(orderRequestDto.getItemCode());

    if(remaining<0){
      itemStockDomainService.incrementAndCountItemStock(orderRequestDto.getItemCode());
      throw new Exception("재고가 부족합니다");// todo : exception 추후 일괄 수정
    }

    try {
      return orderService.saveOrder(orderRequestDto);
    } catch (Exception e) {
      itemStockDomainService.incrementAndCountItemStock(orderRequestDto.getItemCode()); // 주문 실패시 보상 트랜잭션
      throw e;
    }
  }
}
