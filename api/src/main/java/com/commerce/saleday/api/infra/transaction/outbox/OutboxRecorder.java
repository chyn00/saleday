package com.commerce.saleday.api.infra.transaction.outbox;

import com.commerce.saleday.common.outbox.model.OutboxMessage;
import com.commerce.saleday.common.outbox.repository.OutboxRepository;
import com.commerce.saleday.message.stock.DecreaseStockEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OutboxRecorder {

  private final OutboxRepository outboxRepository;

  @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
  public void saveOutbox(DecreaseStockEvent event) {
    OutboxMessage message = OutboxMessage.create(event.getEventId(), "stock", event);
    outboxRepository.save(message);
  }
}