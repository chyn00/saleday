package com.commerce.saleday.api.infra.transaction.outbox;

import com.commerce.saleday.domain.outbox.model.OutboxMessage;
import com.commerce.saleday.domain.outbox.repository.OutboxRepository;
import com.commerce.saleday.domain.stock.port.ItemStockPublisherKafkaPort;
import com.commerce.saleday.message.stock.DecreaseStockEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxScheduler {

  private final OutboxRepository outboxRepository;
  private final ItemStockPublisherKafkaPort itemStockPublisherKafkaPort;

  @Scheduled(fixedDelay = 10000)
  public void resendFailedItemStockEvents() {
    List<OutboxMessage> failedMessages = outboxRepository.findSentFailStockMessage();

    for (OutboxMessage msg : failedMessages) {
      try {
        itemStockPublisherKafkaPort.publishDecreaseStock(msg.getPayloadAs(DecreaseStockEvent.class));
        msg.markSuccess();
      } catch (Exception e) {
        msg.markFailed();
        log.error("재전송 실패: {}", msg.getId(), e);
      }
    }
    outboxRepository.saveAll(failedMessages);
  }
}
