package com.commerce.saleday.api.infra.transaction.listener;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

import com.commerce.saleday.domain.stock.port.ItemStockPublisherKafkaPort;
import com.commerce.saleday.message.stock.DecreaseStockEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OrderEventHandler {

  private final ItemStockPublisherKafkaPort itemStockPublisherKafkaPort;

  //applicationEventPublisher(spring)을 활용하여, 트랜잭션이 commit 되면 pub 되도록 개발
  //정합성에 이점(order가 save commit된 경우에만 pub되기 떄문에)
  @TransactionalEventListener(phase = AFTER_COMMIT)
  public void handleOrderCompleted(DecreaseStockEvent event) {

    try {
      itemStockPublisherKafkaPort.publishDecreaseStock(event);
    } catch (Exception e) {
      //todo: DLQ가 아닌 outbox 패턴으로 재시도 할 수 있도록 로직 작성

      throw new RuntimeException(e);
    }
  }
}
