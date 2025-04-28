package com.commerce.saleday.api.domain.stock.port;

import com.commerce.saleday.message.stock.DecreaseStockEvent;

public interface ItemStockKafkaPort {

  void publishDecreaseStock(DecreaseStockEvent decreaseStockEvent);
}
