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

  //todo: 카프카 listen 에서 배치처리 해서 미리 oom 방지 500개 제한
  //todo: config 설정 연관됨.
  @Override
  @KafkaListener(topics = "stock.decreased", groupId = "stock.decreased.consumer")
  public void decreaseStockListener(List<DecreaseStockEvent> decreaseStockEvents) {

    itemStockService.decreaseStock(decreaseStockEvents);
  }
}
