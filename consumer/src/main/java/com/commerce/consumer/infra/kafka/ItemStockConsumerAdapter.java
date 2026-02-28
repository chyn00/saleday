package com.commerce.consumer.infra.kafka;

import com.commerce.consumer.application.service.ItemStockConsumerService;
import com.commerce.saleday.message.stock.DecreaseStockEvent;
import com.commerce.saleday.order.domain.stock.port.ItemStockConsumerKafkaPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemStockConsumerAdapter implements ItemStockConsumerKafkaPort {

  private final ItemStockConsumerService itemStockConsumerService;

  //500개씩 처리
  @Override
  @KafkaListener(topics = "stock.decreased", groupId = "stock.decreased.consumer", containerFactory = "kafkaListenerContainerFactory")
  public void decreaseStockListener(List<DecreaseStockEvent> decreaseStockEvents) {
    itemStockConsumerService.decreaseStock(decreaseStockEvents);
  }
}
