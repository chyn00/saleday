package com.commerce.saleday.api.infra.kafka;

import com.commerce.saleday.domain.stock.model.ItemStock;
import com.commerce.saleday.domain.stock.port.ItemStockPublisherKafkaPort;
import com.commerce.saleday.message.stock.DecreaseStockEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemStockProducerAdapter implements ItemStockPublisherKafkaPort {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  // 다음과 같은 naming convention 에는
  // event driven 로그 파싱 분석시 startWith stock.처럼 검색하기 편한 이점이 있다.
  public void publishDecreaseStock(ItemStock itemStock){
    DecreaseStockEvent decreaseStockEvent =
        new DecreaseStockEvent(itemStock.getItem().getCode(), itemStock.getQuantity());
    kafkaTemplate.send("stock.decreased", decreaseStockEvent);
  }
}
