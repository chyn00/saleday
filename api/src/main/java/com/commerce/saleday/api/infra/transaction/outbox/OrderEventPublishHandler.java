package com.commerce.saleday.api.infra.transaction.outbox;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

import com.commerce.saleday.common.outbox.model.OutboxMessage;
import com.commerce.saleday.common.outbox.repository.OutboxRepository;
import com.commerce.saleday.message.stock.DecreaseStockEvent;
import com.commerce.saleday.order.domain.stock.port.ItemStockPublisherKafkaPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventPublishHandler {

  private final ItemStockPublisherKafkaPort itemStockPublisherKafkaPort;
  private final OutboxRepository outboxRepository;

  //applicationEventPublisher(spring)을 활용하여, 트랜잭션이 commit 되면 pub 되도록 개발
  //정합성에 이점(order가 save commit된 경우에만 pub되기 떄문에)
  @TransactionalEventListener(phase = AFTER_COMMIT)
  public void handleOrderCompleted(DecreaseStockEvent event) {

    // pending 처리 후 카프카 ACK를 활용한 success, fail 처리
    itemStockPublisherKafkaPort.publishDecreaseStock(event);
  }
}
