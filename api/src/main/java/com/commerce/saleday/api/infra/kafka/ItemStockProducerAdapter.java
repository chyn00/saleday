package com.commerce.saleday.api.infra.kafka;

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

  private final OutboxRepository outboxRepository;
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
      // OutBoxMessage 호출(콜백 내부에서 실행해줘야 같은 쓰레드 요청 내 처리)
      OutboxMessage outboxMessage = outboxRepository.findById(decreaseStockEvent.getEventId())
          .orElseThrow(() -> new EntityNotFoundException("OutboxMessage not found. id=" + decreaseStockEvent.getEventId()));

      if (ex == null) {
        //ack 리턴 받은 경우
        outboxMessage.markSuccess();
//        log.info("✅ Kafka ACK Success: {}", result.getRecordMetadata());
      } else {
        // ack 리턴 받지 못한 경우
        outboxMessage.markFailed();
//        log.error("❌ Kafka ACK Failed: {}", ex.getMessage(), ex);
        // DLQ 전환 또는 재시도 로직도 이 안에
      }

      outboxRepository.save(outboxMessage);
    });
  }
}
