package com.commerce.saleday.api.service.orchestrator;

import com.commerce.saleday.api.controller.order.model.OrderRequestDto;
import com.commerce.saleday.common.exception.ExceptionCode;
import com.commerce.saleday.common.exception.SaleDayException;
import com.commerce.saleday.order.domain.stock.port.ItemStockPublisherKafkaPort;
import com.commerce.saleday.order.service.order.OrderService;
import com.commerce.saleday.order.service.stock.ItemStockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 고트래픽 대응을 위한 재고 수량 감소, 트랜잭션 등을 처리하기 위한 Order 퍼사드 서비스
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderOrchestratorService {

  private final ItemStockPublisherKafkaPort itemStockPublisherKafkaPort;
  private final ItemStockService itemStockService;
  private final OrderService orderService;
  private final ApplicationEventPublisher applicationEventPublisher;

  //고트래픽 대용으로 제한된 재고에서 동시성제어로 주문하며, 주문 코드를 리턴한다.
  //레디스의 트랜잭션은 보상트랜잭션을 활용한다.
  @Transactional
  public String orderWithLimitedStock(OrderRequestDto orderRequestDto) throws Exception {

    long remaining = itemStockService.decrementAndCountItemStock(orderRequestDto.getItemCode());

    if (remaining < 0) {
      itemStockService.incrementAndCountItemStock(orderRequestDto.getItemCode());
      throw new SaleDayException(ExceptionCode.OUT_OF_STOCK);
    }

    try {
      //saveOrder에는 트랜잭션이 물리지 않기 떄문에 카프카로 넘겨주는 거 명시
      String orderCode = orderService.saveOrder(orderRequestDto.toCommand());

      //주문이 성공한 경우에, decrease item Stock pub 실행
      //주문이 성공해야만 재고가 감소되며, 이미 redis 에서 감소되었기 때문에 실패할 경우 outBox pattern으로 재시도
      //주문의 성공 여부는 transaction이 commit된 시점에 pub이 실행되도록함.(OrderEventHandler)
      applicationEventPublisher.publishEvent(orderRequestDto.toDecreaseStockEvent());

      return orderCode;
    } catch (Exception e) {
      log.error("order fail : " + e.getMessage());
      throw e;
    }
  }
}
