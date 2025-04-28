package com.commerce.saleday.api.domain.stock.port;

public interface ItemStockKafkaPort {

  void publishDecreaseStock(Long id);
}
