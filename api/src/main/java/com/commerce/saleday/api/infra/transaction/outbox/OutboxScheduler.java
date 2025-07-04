package com.commerce.saleday.api.infra.transaction.outbox;

import com.commerce.saleday.common.outbox.model.OutboxMessage;
import com.commerce.saleday.common.outbox.repository.OutboxRepository;
import com.commerce.saleday.message.stock.DecreaseStockEvent;
import com.commerce.saleday.order.domain.stock.port.ItemStockPublisherKafkaPort;
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

  @Scheduled(cron = "0 0/30 * * * *")//정합성을 위해, 30분 마다 스케쥴링
  public void resendFailedItemStockEvents() {
    List<OutboxMessage> failedMessages = outboxRepository.findSentFailStockMessage();

    //실패된 메세지 재전송
    for (OutboxMessage msg : failedMessages) {
      try {
        itemStockPublisherKafkaPort.publishDecreaseStock(msg.getPayloadAs(DecreaseStockEvent.class));
        msg.markSuccess();//재전송 성공
      } catch (Exception e) {
        msg.markFailed();// 재전송 실패
        log.error("재전송 실패: {}", msg.getId(), e);
      }
    }
    outboxRepository.saveAll(failedMessages);//기록을 위해, 성공/실패 모두 저장, 변경감지로 업데이트 되어있음
  }
}
