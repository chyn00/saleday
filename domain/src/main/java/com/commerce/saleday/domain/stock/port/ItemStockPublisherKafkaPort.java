package com.commerce.saleday.domain.stock.port;


import com.commerce.saleday.domain.stock.model.ItemStock;

public interface ItemStockPublisherKafkaPort {

  void publishDecreaseStock(ItemStock itemStock);
}
