package com.commerce.saleday.api.infra.kafka;

import com.commerce.saleday.api.infra.transaction.outbox.OutboxStatusService;
import com.commerce.saleday.message.stock.DecreaseStockEvent;
import com.commerce.saleday.order.domain.stock.port.ItemStockPublisherKafkaPort;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class ItemStockProducerAdapter implements ItemStockPublisherKafkaPort {

  private final OutboxStatusService outboxStatusService;
  private final KafkaTemplate<String, Object> kafkaTemplate;
  @Qualifier("kafkaPublishExecutor")
  private final Executor kafkaPublishExecutor;

  @Qualifier("outboxCallbackExecutor")
  private final Executor outboxCallbackExecutor;


  // 쓰레드를 기다리지 않음(ex. completableFuture get, allof 등 사용하지 않아서 비동기 처리)
  // 다음과 같은 naming convention 에는
  // event driven 로그 파싱 분석시 startWith stock.처럼 검색하기 편한 이점이 있다.
  public void publishDecreaseStock(DecreaseStockEvent decreaseStockEvent) {

    /**
     1. publisher : eventId를 활용해 ack가 리턴 여부에 따라 상태값을 update.
     2. consumer(보내기 전 전처리) : 멱등성을 위해 eventId도 함께 전송.
     **/
    decreaseStockEvent.initEventId(decreaseStockEvent.getEventId());
    final String eventId = decreaseStockEvent.getEventId();
    CompletableFuture.runAsync(() -> {
      // 콜백패턴을 활용한 ack 응답여부 확인용 세팅
      CompletableFuture<SendResult<String, Object>> future;
      try {
        future = kafkaTemplate.send("stock.decreased", decreaseStockEvent);
      } catch (Exception sendInvocationEx) {
        log.error("Kafka send invocation failed before callback registration. eventId={}", eventId,
            sendInvocationEx);
        safeMarkFailed(eventId);
        return;
      }

      // whenCompleteAsync는 completableFuture의 다른 쓰레드와 분리를 시켜 실행되도록하여서, 비동기의 장점을 더 살린다.
      // whenComplete를 사용하면 완료된 그쓰레드에서 다음 콜백이 실행된다.
      // 따라서, 아웃박스 패턴의 장점을 살리기 위해, 비교적 DB의 네트워크 부하가 걸리는 부분을 별도의 쓰레드 풀로 분리한다.
      future.handleAsync((result, ex) -> {
        if (ex == null) {
          safeMarkSuccess(eventId);
        } else {
          safeMarkFailed(eventId);
          // Optional: DLQ 처리 or alert
          log.error("Outbox callback processing failed. eventId={}",
              eventId,
              ex);
        }
        return null;
      }, outboxCallbackExecutor)
          .exceptionally(callbackStageEx -> {
            log.error("Outbox callback stage failed. eventId={}", eventId, callbackStageEx);
            safeMarkFailed(eventId);
            return null;
          }); // callback에 대한 별도 쓰레드 풀 관리 -> DB연결이라서 안정성이 먼저

    }, kafkaPublishExecutor).exceptionally(publishStageEx -> {
      log.error("Kafka publish async stage failed. eventId={}", eventId, publishStageEx);
      safeMarkFailed(eventId);
      return null;
    });
  }

  private void safeMarkSuccess(String eventId) {
    try {
      outboxStatusService.markSuccess(eventId);
    } catch (Exception markSuccessEx) {
      log.error("Failed to mark outbox success. eventId={}", eventId, markSuccessEx);
    }
  }

  private void safeMarkFailed(String eventId) {
    try {
      outboxStatusService.markFailed(eventId);
    } catch (Exception markFailedEx) {
      log.error("Failed to mark outbox failed. eventId={}", eventId, markFailedEx);
    }
  }
}
