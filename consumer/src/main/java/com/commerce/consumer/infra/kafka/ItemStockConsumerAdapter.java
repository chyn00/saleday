package com.commerce.consumer.infra.kafka;

import com.commerce.consumer.application.service.ItemStockConsumerService;
import com.commerce.saleday.message.stock.DecreaseStockEvent;
import com.commerce.saleday.order.domain.stock.port.ItemStockConsumerKafkaPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemStockConsumerAdapter implements ItemStockConsumerKafkaPort {

  private final ItemStockConsumerService itemStockConsumerService;

  //500개씩 처리
  @Override
  @KafkaListener(topics = "stock.decreased", groupId = "stock.decreased.consumer", containerFactory = "kafkaListenerContainerFactory")
  public void decreaseStockListener(List<DecreaseStockEvent> decreaseStockEvents) {

    try {
      itemStockConsumerService.decreaseStock(decreaseStockEvents);
    } catch (InterruptedException e) {
      // Kafka Listener는 별도 워커 스레드에서 동작하고, 셧다운/취소 시 interrupt 신호를 받을 수 있다.
      // 블로킹 구간(예: lock 대기, sleep, wait)에서 interrupt가 들어오면 InterruptedException이 발생한다.
      // 이 예외는 일반 실패보다 "중단 요청" 의미가 중요해서 별도 catch로 명시 처리한다.
      // catch 이후 인터럽트 상태가 사라질 수 있으므로 반드시 interrupt 상태를 복구해야
      // 상위 컨테이너가 중단 신호를 인지하고 종료/재시도 정책을 올바르게 적용할 수 있다.
      // TODO: DLQ/재시도 정책을 확장할 때도 InterruptedException은 여기서 삼키지 않고 전파한다.
      //       그래야 DefaultErrorHandler -> DLQ 경로가 유지되고, 인터럽트 의미도 보존된다.
      Thread.currentThread().interrupt();
      log.error("Interrupted while consuming stock decrease events. batchSize={}",
          decreaseStockEvents.size(), e);
      throw new IllegalStateException("Interrupted while processing stock decrease events", e);
    } catch (Exception e) {
      log.error("Failed to consume stock decrease events. batchSize={}",
          decreaseStockEvents.size(), e);
      throw e;
    }
  }
}
