package com.commerce.saleday.api.infra.kafka;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.commerce.saleday.api.infra.transaction.outbox.OutboxStatusService;
import com.commerce.saleday.message.stock.DecreaseStockEvent;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

@ExtendWith(MockitoExtension.class)
class ItemStockProducerAdapterTest {

  @Mock
  private OutboxStatusService outboxStatusService;

  @Mock
  private KafkaTemplate<String, Object> kafkaTemplate;

  private final Executor directExecutor = Runnable::run;

  private ItemStockProducerAdapter itemStockProducerAdapter;

  @BeforeEach
  void setUp() {
    itemStockProducerAdapter = new ItemStockProducerAdapter(
        outboxStatusService,
        kafkaTemplate,
        directExecutor,
        directExecutor
    );
  }

  @Test
  @DisplayName("Kafka 전송 성공 시 Outbox 상태를 SUCCESS로 마킹한다")
  void publishDecreaseStock_marksSuccess_whenKafkaSendSucceeds() {
    DecreaseStockEvent event = DecreaseStockEvent.toEventMessage("ITEM-1", 1L);
    event.initEventId("EVENT-1");

    CompletableFuture<SendResult<String, Object>> future = CompletableFuture.completedFuture(null);
    when(kafkaTemplate.send("stock.decreased", event)).thenReturn(future);

    itemStockProducerAdapter.publishDecreaseStock(event);

    verify(outboxStatusService).markSuccess("EVENT-1");
    verify(outboxStatusService, never()).markFailed("EVENT-1");
  }

  @Test
  @DisplayName("Kafka 전송 실패 시 Outbox 상태를 FAILED로 마킹한다")
  void publishDecreaseStock_marksFailed_whenKafkaSendFails() {
    DecreaseStockEvent event = DecreaseStockEvent.toEventMessage("ITEM-1", 1L);
    event.initEventId("EVENT-2");

    CompletableFuture<SendResult<String, Object>> failedFuture = new CompletableFuture<>();
    failedFuture.completeExceptionally(new RuntimeException("kafka send failed"));

    when(kafkaTemplate.send("stock.decreased", event)).thenReturn(failedFuture);

    itemStockProducerAdapter.publishDecreaseStock(event);

    verify(outboxStatusService).markFailed("EVENT-2");
    verify(outboxStatusService, never()).markSuccess("EVENT-2");
  }
}
