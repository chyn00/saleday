package com.commerce.saleday.domain.stock.port;


import com.commerce.saleday.message.stock.DecreaseStockEvent;

public interface ItemStockPublisherKafkaPort {

  void publishDecreaseStock(DecreaseStockEvent decreaseStockEvent);
}
