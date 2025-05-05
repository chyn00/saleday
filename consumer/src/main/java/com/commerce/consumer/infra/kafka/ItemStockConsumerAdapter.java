package com.commerce.consumer.infra.kafka;

import com.commerce.consumer.application.service.ItemStockService;
import com.commerce.saleday.domain.stock.port.ItemStockConsumerKafkaPort;
import com.commerce.saleday.message.stock.DecreaseStockEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemStockConsumerAdapter implements ItemStockConsumerKafkaPort {

  private final ItemStockService itemStockService;

  //500개씩 처리
  @Override
  @KafkaListener(topics = "stock.decreased", groupId = "stock.decreased.consumer", containerFactory = "kafkaListenerContainerFactory")
  public void decreaseStockListener(List<DecreaseStockEvent> decreaseStockEvents) {
    try {
      itemStockService.decreaseStock(decreaseStockEvents);
    } catch (Exception e) {
      // 필요 시 재처리 로직 or DLQ 연동
    }
  }
}
