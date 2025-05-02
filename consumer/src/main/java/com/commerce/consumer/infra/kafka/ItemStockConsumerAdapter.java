package com.commerce.consumer.infra.kafka;

import com.commerce.saleday.domain.stock.port.ItemStockConsumerKafkaPort;
import com.commerce.saleday.message.stock.DecreaseStockEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ItemStockConsumerAdapter implements ItemStockConsumerKafkaPort {

  @Override
  @KafkaListener(topics = "stock.decreased", groupId = "stock.decreased.consumer")
  public void decreaseStockListener(DecreaseStockEvent decreaseStockEvent) {
      log.info("listen completed!");
      log.info(decreaseStockEvent.toString());
      log.info("--------");
  }
}
