package com.commerce.saleday.api.infra.kafka;

import com.commerce.saleday.api.infra.transaction.outbox.OutboxStatusService;
import com.commerce.saleday.common.outbox.model.OutboxMessage;
import com.commerce.saleday.common.outbox.repository.OutboxRepository;
import com.commerce.saleday.message.stock.DecreaseStockEvent;
import com.commerce.saleday.order.domain.stock.port.ItemStockPublisherKafkaPort;
import jakarta.persistence.EntityNotFoundException;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class ItemStockProducerAdapter implements ItemStockPublisherKafkaPort {

  private final OutboxStatusService outboxStatusService;
  private final KafkaTemplate<String, Object> kafkaTemplate;

  // 쓰레드를 기다리지 않음(ex. completableFuture get, allof 등 사용하지 않아서 비동기 처리)
  // 다음과 같은 naming convention 에는
  // event driven 로그 파싱 분석시 startWith stock.처럼 검색하기 편한 이점이 있다.
  public void publishDecreaseStock(DecreaseStockEvent decreaseStockEvent){

    /**
       1. publisher : eventId를 활용해 ack가 리턴 여부에 따라 상태값을 update.
       2. consumer(보내기 전 전처리) : 멱등성을 위해 eventId도 함께 전송.
     **/
    decreaseStockEvent.initEventId(decreaseStockEvent.getEventId());

    // 콜백패턴을 활용한 ack 응답여부 확인용 세팅
    CompletableFuture<SendResult<String, Object>> future =
        kafkaTemplate.send("stock.decreased", decreaseStockEvent);

    future.whenComplete((result, ex) -> {
      if (ex == null) {
        outboxStatusService.markSuccess(decreaseStockEvent.getEventId());
      } else {
        outboxStatusService.markFailed(decreaseStockEvent.getEventId());
        // Optional: DLQ 처리 or alert
      }
    });
  }
}
