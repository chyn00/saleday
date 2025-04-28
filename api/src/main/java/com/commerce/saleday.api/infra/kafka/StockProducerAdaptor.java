package com.commerce.saleday.api.infra.kafka;

import com.commerce.saleday.api.domain.stock.port.ItemStockKafkaPort;
import com.commerce.saleday.message.stock.DecreaseStockEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockProducerAdaptor implements ItemStockKafkaPort {

  private final KafkaTemplate<String, DecreaseStockEvent> kafkaTemplate;

  // 다음과 같은 naming convention 에는
  // event driven 로그 파싱 분석시 startWith stock.처럼 검색하기 편한 이점이 있다.
  public void publishDecreaseStock(DecreaseStockEvent decreaseStockEvent){
    kafkaTemplate.send("stock.decreased", decreaseStockEvent);
  }
}
